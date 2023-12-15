package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.post.service.ThumbnailService;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.TradeCooltimeException;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateTradeDto;
import com.dku.council.domain.with_dankook.model.entity.TradeImage;
import com.dku.council.domain.with_dankook.model.entity.type.Trade;
import com.dku.council.domain.with_dankook.repository.TradeRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookMemoryRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.UploadedImage;
import com.dku.council.infra.nhn.s3.service.ImageUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    public static final String TRADE_KEY = "trade";

    private final TradeRepository tradeRepository;
    private final WithDankookMemoryRepository withDankookMemoryRepository;
    private final UserRepository userRepository;

    private final ImageUploadService imageUploadService;
    private final ThumbnailService thumbnailService;

    private final Clock clock;

    @Value("${app.with-dankook.trade.write-cooltime}")
    private final Duration writeCooltime;

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
}
