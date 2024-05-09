package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.chat.exception.ChatRoomNotFoundException;
import com.dku.council.domain.chat.model.dto.response.ResponseChatRoomIdDto;
import com.dku.council.domain.chat.repository.ChatRoomRepository;
import com.dku.council.domain.chat.service.ChatService;
import com.dku.council.domain.post.exception.PostCooltimeException;
import com.dku.council.domain.post.repository.PostTimeMemoryRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.WithDankookNotFoundException;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedBearEatsDto;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedBearEatsPossibleReviewDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateBearEatsDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleBearEatsDto;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import com.dku.council.domain.with_dankook.model.entity.type.BearEats;
import com.dku.council.domain.with_dankook.repository.with_dankook.BearEatsRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookUserRepository;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BearEatsService {
    public static final String BEAR_EATS_KEY = "bearEats";

    private final WithDankookService<BearEats> withDankookService;
    private final WithDankookUserService withDankookuserSerivce;
    private final ChatService chatService;

    private final BearEatsRepository bearEatsRepository;
    private final WithDankookUserRepository withDankookUserRepository;
    private final UserRepository userRepository;
    private final PostTimeMemoryRepository postTimeMemoryRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final Clock clock;

    @Value("${app.with-dankook.bear-eats.write-cooltime}")
    private final Duration writeCooltime;

    @Transactional
    public Long create(Long userId, RequestCreateBearEatsDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Instant now = Instant.now(clock);
        if (postTimeMemoryRepository.isAlreadyContains(BEAR_EATS_KEY, userId, now)) {
            throw new PostCooltimeException("bear-eats");
        }

        Long result = withDankookService.create(bearEatsRepository, userId, dto);

        WithDankookUser withDankookuser = WithDankookUser.builder()
                        .user(user)
                        .withDankook(bearEatsRepository.findById(result).orElseThrow(WithDankookNotFoundException::new))
                        .build();
        withDankookUserRepository.save(withDankookuser);

        BearEats bearEats = bearEatsRepository.findById(result).orElseThrow(WithDankookNotFoundException::new);

        // 해당 게시글에 대한 채팅방 생성
        chatService.createChatRoom(bearEats, dto.getRestaurant(), 4, userId);

        postTimeMemoryRepository.put(BEAR_EATS_KEY, userId, writeCooltime, now);
        return result;
    }

    public Page<SummarizedBearEatsDto> list(Pageable pageable, int bodySize) {
        Page<BearEats> result = bearEatsRepository.findAll(pageable);
        return result.map((bearEats) -> new SummarizedBearEatsDto(withDankookService.makeListDto(bodySize, bearEats), bearEats,
                withDankookuserSerivce.recruitedCount(withDankookService.makeListDto(bodySize, bearEats).getId())));
    }

    public Page<SummarizedBearEatsDto> listMyPosts(Long userId, Pageable pageable) {
        return bearEatsRepository.findAllBearEatsByUserId(userId, pageable)
                .map(bearEats -> new SummarizedBearEatsDto(withDankookService.makeListDto(50, bearEats), bearEats,
                        withDankookuserSerivce.recruitedCount(withDankookService.makeListDto(50, bearEats).getId())));
    }

    @Transactional(readOnly = true)
    public Page<SummarizedBearEatsPossibleReviewDto> listMyPossibleReviewPosts(Long userId, Pageable pageable) {
        List<SummarizedBearEatsPossibleReviewDto> list = bearEatsRepository.findAllPossibleReviewPost(userId, pageable)
                .map(bearEats -> {
                    SummarizedBearEatsPossibleReviewDto dto = new SummarizedBearEatsPossibleReviewDto(bearEats, userId);
                    if (!dto.getTargetUserList().isEmpty()) {
                        return dto;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .stream().collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    public ResponseSingleBearEatsDto findOne(Long id, Long userId, UserRole role) {
        BearEats bearEats = findBearEats(bearEatsRepository, id, role);
        return new ResponseSingleBearEatsDto(withDankookService.makeSingleDto(userId, bearEats), bearEats,
                withDankookuserSerivce.recruitedCount(withDankookService.makeSingleDto(userId, bearEats).getId()));
    }

    private BearEats findBearEats(BearEatsRepository bearEatsRepository, Long id, UserRole role) {
        Optional<BearEats> bearEats;
        if (role.isAdmin()) {
            bearEats = bearEatsRepository.findWithAllStatusById(id);
        } else {
            bearEats = bearEatsRepository.findWithNotDeletedById(id);
        }
        return bearEats.orElseThrow(WithDankookNotFoundException::new);
    }

    @Transactional
    public ResponseChatRoomIdDto enter(Long id, Long userId, UserRole userRole) {
        String roomId = chatRoomRepository.findChatRoomByWithDankookId(id).orElseThrow(ChatRoomNotFoundException::new).getRoomId();
        withDankookService.enter(bearEatsRepository, id, userId, userRole);

        return new ResponseChatRoomIdDto(roomId);
    }

    @Transactional
    public void delete(Long id, Long userId, boolean isAdmin) {
        withDankookService.delete(bearEatsRepository, id, userId, isAdmin);
    }

    @Transactional
    public void close(Long tradeId, Long userId) {
        bearEatsRepository.findById(tradeId).ifPresent(eatingAlone -> {
            if (eatingAlone.getMasterUser().getId().equals(userId)) {
                eatingAlone.close();
            } else{
                throw new NotGrantedException();
            }
        });
    }
}
