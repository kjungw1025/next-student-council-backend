package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.chat.service.ChatService;
import com.dku.council.domain.post.exception.PostCooltimeException;
import com.dku.council.domain.post.repository.PostTimeMemoryRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.WithDankookNotFoundException;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedEatingAloneDto;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedEatingAlonePossibleReviewDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateEatingAloneDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleEatingAloneDto;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import com.dku.council.domain.with_dankook.model.entity.type.EatingAlone;
import com.dku.council.domain.with_dankook.repository.with_dankook.EatingAloneRepository;
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
public class EatingAloneService {

    public static final String EATING_ALONG_KEY = "eatingAlong";

    private final WithDankookService<EatingAlone> withDankookService;
    private final WithDankookUserService withDankookUserService;
    private final ChatService chatService;

    private final EatingAloneRepository eatingAloneRepository;
    private final WithDankookUserRepository withDankookUserRepository;
    private final PostTimeMemoryRepository postTimeMemoryRepository;
    private final UserRepository userRepository;

    private final Clock clock;

    @Value("${app.with-dankook.eating-alone.write-cooltime}")
    private final Duration writeCooltime;

    @Transactional
    public Long create(Long userId, RequestCreateEatingAloneDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Instant now = Instant.now(clock);
        if (postTimeMemoryRepository.isAlreadyContains(EATING_ALONG_KEY, userId, now)) {
            throw new PostCooltimeException("eating-alone");
        }

        Long result = withDankookService.create(eatingAloneRepository, userId, dto);

        WithDankookUser withDankookUser = WithDankookUser.builder()
                .user(user)
                .withDankook(eatingAloneRepository.findById(result).orElseThrow(WithDankookNotFoundException::new))
                .build();
        withDankookUserRepository.save(withDankookUser);

        EatingAlone eatingAlone = eatingAloneRepository.findById(result).orElseThrow(WithDankookNotFoundException::new);

        // 해당 게시글에 대한 채팅방 생성
        chatService.createChatRoom(eatingAlone, dto.getTitle(), 4, userId);

        postTimeMemoryRepository.put(EATING_ALONG_KEY, userId, writeCooltime, now);
        return result;
    }

    public Page<SummarizedEatingAloneDto> list(Pageable pageable, int bodySize) {
        Page<EatingAlone> result = eatingAloneRepository.findAll(pageable);
        return result.map(eatingAlone -> new SummarizedEatingAloneDto(withDankookService.makeListDto(bodySize, eatingAlone), eatingAlone,
                withDankookUserService.recruitedCount(withDankookService.makeListDto(bodySize, eatingAlone).getId())));
    }

    public Page<SummarizedEatingAloneDto> listMyPosts(Long userId, Pageable pageable) {
        return eatingAloneRepository.findAllEatingAloneByUserId(userId, pageable)
                .map(eatingAlone -> new SummarizedEatingAloneDto(withDankookService.makeListDto(50, eatingAlone), eatingAlone,
                        withDankookUserService.recruitedCount(withDankookService.makeListDto(50, eatingAlone).getId())));
    }

    @Transactional(readOnly = true)
    public Page<SummarizedEatingAlonePossibleReviewDto> listMyPossibleReviewPosts(Long userId, Pageable pageable) {
        List<SummarizedEatingAlonePossibleReviewDto> list = eatingAloneRepository.findAllPossibleReviewPost(userId, pageable)
                .map(eatingAlone -> {
                    SummarizedEatingAlonePossibleReviewDto dto = new SummarizedEatingAlonePossibleReviewDto(eatingAlone, userId);
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

    public ResponseSingleEatingAloneDto findOne(Long id, Long userId, UserRole role) {
        EatingAlone eatingAlone = findEatingAlone(eatingAloneRepository, id, role);
        return new ResponseSingleEatingAloneDto(withDankookService.makeSingleDto(userId, eatingAlone), eatingAlone,
                withDankookUserService.recruitedCount(withDankookService.makeSingleDto(userId, eatingAlone).getId()));
    }

    private EatingAlone findEatingAlone(EatingAloneRepository eatingAloneRepository, Long id, UserRole role) {
        Optional<EatingAlone> eatingAlone;
        if (role == UserRole.ADMIN) {
            eatingAlone = eatingAloneRepository.findWithClosedById(id);
        } else {
            eatingAlone = eatingAloneRepository.findById(id);
        }
        return eatingAlone.orElseThrow(WithDankookNotFoundException::new);
    }

    @Transactional
    public void enter(Long id, Long userId, UserRole role) {
        withDankookService.enter(eatingAloneRepository, id, userId, role);
    }

    @Transactional
    public void delete(Long id, Long userId, boolean isAdmin) {
        withDankookService.delete(eatingAloneRepository, id, userId, isAdmin);
    }

    @Transactional
    public void close(Long tradeId, Long userId) {
        eatingAloneRepository.findById(tradeId).ifPresent(eatingAlone -> {
            if (eatingAlone.getMasterUser().getId().equals(userId)) {
                eatingAlone.close();
            } else{
                throw new NotGrantedException();
            }
        });
    }
}
