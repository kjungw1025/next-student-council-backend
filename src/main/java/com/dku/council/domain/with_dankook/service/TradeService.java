package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.like.service.LikeService;
import com.dku.council.domain.post.service.ThumbnailService;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.ImageSizeExceededException;
import com.dku.council.domain.with_dankook.exception.TradeCooltimeException;
import com.dku.council.domain.with_dankook.exception.WithDankookNotFoundException;
import com.dku.council.domain.with_dankook.model.WithDankookStatus;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedTradeDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateTradeDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleTradeDto;
import com.dku.council.domain.with_dankook.model.entity.TradeImage;
import com.dku.council.domain.with_dankook.model.entity.type.Trade;
import com.dku.council.domain.with_dankook.repository.TradeRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookMemoryRepository;
import com.dku.council.domain.with_dankook.repository.spec.WithDankookSpec;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.UploadedImage;
import com.dku.council.infra.nhn.s3.service.ImageUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectUploadContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TradeService {

    public static final String TRADE_KEY = "trade";

    private final TradeRepository tradeRepository;
    private final WithDankookMemoryRepository withDankookMemoryRepository;
    private final UserRepository userRepository;

    private final WithDankookService<Trade> withDankookService;
    private final LikeService likeService;
    private final ImageUploadService imageUploadService;
    private final ThumbnailService thumbnailService;
    private final ObjectUploadContext objectUploadContext;
    private final MessageSource messageSource;

    private final Clock clock;

    @Value("${app.with-dankook.trade.write-cooltime}")
    private final Duration writeCooltime;

    @Transactional
    public Long create(Long userId, RequestCreateTradeDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Instant now = Instant.now(clock);
        if (withDankookMemoryRepository.isAlreadyContains(TRADE_KEY, userId, now)) {
            throw new TradeCooltimeException("trade");
        }

        Trade trade = Trade.builder()
                .user(user)
                .title(dto.getTitle())
                .price(dto.getPrice())
                .content(dto.getContent())
                .tradePlace(dto.getTradePlace())
                .build();

        attachImages(trade, dto.getImages());

        Long result = tradeRepository.save(trade).getId();

        withDankookMemoryRepository.put(TRADE_KEY, userId, writeCooltime, now);
        return result;
    }

    private void attachImages(Trade trade, List<MultipartFile> dtoImages) {
        if (dtoImages.size() > 10) {
            throw new ImageSizeExceededException();
        }
        List<UploadedImage> images = imageUploadService.newContext().uploadImages(
                ImageRequest.ofList(dtoImages),
                trade.getClass().getSimpleName());

        ImageUploadService.Context uploadCtx = imageUploadService.newContext();
        List<TradeImage> tradeImages = new ArrayList<>();

        for (UploadedImage image : images) {
            TradeImage.TradeImageBuilder builder = TradeImage.builder()
                    .fileName(image.getOriginalName())
                    .mimeType(image.getMimeType().toString())
                    .fileId(image.getImageId());

            String thumbnailId = thumbnailService.createThumbnail(uploadCtx, image);
            if (thumbnailId != null) {
                builder.thumbnailId(thumbnailId);
            }
            tradeImages.add(builder.build());
        }

        for(TradeImage tradeImage : tradeImages) {
            tradeImage.changeTrade(trade);
        }
    }

    @Transactional(readOnly = true)
    public Page<SummarizedTradeDto> list(String keyword, Pageable pageable, int bodySize) {
        Specification<Trade> spec = WithDankookSpec.withTitleOrBody(keyword);
        spec = spec.and(WithDankookSpec.withActive());
        Page<Trade> result = tradeRepository.findAll(spec, pageable);
        return result.map((trade) ->
                new SummarizedTradeDto(withDankookService.makeListDto(bodySize, trade), trade, objectUploadContext, messageSource));
    }

    @Transactional(readOnly = true)
    public ResponseSingleTradeDto findOne(Long tradeId, Long userId, UserRole role) {
        Trade trade = findTrade(tradeRepository, tradeId, role);
        return new ResponseSingleTradeDto(withDankookService.makeSingleDto(userId, trade), trade, objectUploadContext, messageSource);
    }

    private Trade findTrade(TradeRepository tradeRepository, Long tradeId, UserRole role) {
        Optional<Trade> trade;
        if (role.isAdmin()) {
            trade = tradeRepository.findWithAllStatusById(tradeId);
        } else {
            trade = tradeRepository.findWithNotDeletedById(tradeId);
        }
        return trade.orElseThrow(WithDankookNotFoundException::new);
    }

    @Transactional
    public void delete(Long tradeId, Long userId, boolean isAdmin) {
        withDankookService.delete(tradeRepository, tradeId, userId, isAdmin);
    }

    @Transactional
    public void close(Long tradeId, Long userId) {
        tradeRepository.findById(tradeId).ifPresent(trade -> {
            if (trade.getMasterUser().getId().equals(userId)) {
                trade.close();
            } else{
                throw new NotGrantedException();
            }
        });
    }

    @Transactional(readOnly = true)
    public Page<SummarizedTradeDto> listMyPosts(Long userId, Pageable pageable) {
        return tradeRepository.findAllByUserId(userId, pageable)
                .map(trade -> new SummarizedTradeDto(withDankookService.makeListDto(50, trade), trade, objectUploadContext, messageSource));
    }
}
