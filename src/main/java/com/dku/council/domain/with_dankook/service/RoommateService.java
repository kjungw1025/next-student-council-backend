package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.*;
import com.dku.council.domain.with_dankook.model.ParticipantStatus;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedRoommateDto;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedRoommatePossibleReviewDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateRoommateDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleRoommateDto;
import com.dku.council.domain.with_dankook.model.entity.RoomMateSurvey;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import com.dku.council.domain.with_dankook.model.entity.type.Roommate;
import com.dku.council.domain.with_dankook.repository.RoomMateSurveyRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookUserRepository;
import com.dku.council.domain.with_dankook.repository.with_dankook.RoommateRepository;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoommateService {

    private final RoommateRepository roommateRepository;
    private final WithDankookUserRepository withDankookUserRepository;
    private final UserRepository userRepository;
    private final RoomMateSurveyRepository surveyRepository;

    private final WithDankookService<Roommate> withDankookService;
    private final WithDankookUserService withDankookUserService;

    private final MessageSource messageSource;

    @Transactional(readOnly = true)
    public Page<SummarizedRoommateDto> list(Long userId, Pageable pageable, int bodySize) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        RoomMateSurvey userSurvey = surveyRepository.findByUserId(userId).orElseThrow(RoommateSurveyNotFoundException::new);
        Page<Roommate> list = roommateRepository.findAllByUserGender(user.getGender(), pageable);
        return list.map((roommate) -> new SummarizedRoommateDto(
                withDankookService.makeListDto(bodySize, roommate),
                roommate,
                messageSource,
                withDankookUserService.recruitedCount(withDankookService.makeListDto(bodySize, roommate).getId()),
                checkEqualCount(userSurvey, surveyRepository.findByUserId(roommate.getMasterUser().getId()).orElseThrow(RoommateSurveyNotFoundException::new)),
                checkIsMine(userId, roommate)
        ));
    }

    @Transactional(readOnly = true)
    public Page<SummarizedRoommatePossibleReviewDto> listMyPossibleReviewPosts(Long userId, Pageable pageable) {
        List<SummarizedRoommatePossibleReviewDto> list = roommateRepository.findAllPossibleReviewPost(userId, pageable)
                .map(roommate -> {
                    SummarizedRoommatePossibleReviewDto dto = new SummarizedRoommatePossibleReviewDto(roommate, userId);
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

    @Transactional(readOnly = true)
    public ResponseSingleRoommateDto findOne(Long roommateId, Long userId, UserRole role) {
        Roommate roommate = findRoommate(roommateRepository, roommateId, role);
        RoomMateSurvey survey = surveyRepository.findByUserId(roommate.getMasterUser().getId()).orElseThrow(RoommateSurveyNotFoundException::new);
        return new ResponseSingleRoommateDto(
                withDankookService.makeSingleDto(userId, roommate), roommate, survey, messageSource
        );
    }

    private Roommate findRoommate(RoommateRepository roommateRepository, Long roommateId, UserRole role) {
        Optional<Roommate> roommate;
        if (role.isAdmin()) {
            roommate = roommateRepository.findWithAllStatusById(roommateId);
        } else {
            roommate = roommateRepository.findWithNotDeletedById(roommateId);
        }
        return roommate.orElseThrow(WithDankookNotFoundException::new);
    }

    @Transactional
    public Long create(Long userId, RequestCreateRoommateDto dto) {
        roommateRepository.findByUserId(userId).ifPresent((roommate) -> {
            throw new AlreadyWrittenRoommateException();
        });
        Long result = withDankookService.create(roommateRepository, userId, dto);
        WithDankookUser withDankookUser = WithDankookUser.builder()
                .withDankook(roommateRepository.findById(result).orElseThrow(WithDankookNotFoundException::new))
                .user(userRepository.findById(userId).orElseThrow(UserNotFoundException::new))
                .build();
        withDankookUserRepository.save(withDankookUser);

        return result;
    }

    @Transactional
    public void apply(Long userId, Long roommateId, UserRole role) {
        Roommate roommate = findRoommate(roommateRepository, roommateId, role);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        validateApplicant(roommate, user);

        WithDankookUser applicant = WithDankookUser.builder()
                .withDankook(roommate)
                .user(user)
                .build();
        applicant.changeStatusToWaiting();
        withDankookUserRepository.save(applicant);
    }

    @Transactional
    public void approve(Long writerId, Long roommateId, Long targetUserId) {
        Roommate roommate = roommateRepository.findById(roommateId).orElseThrow(WithDankookNotFoundException::new);
        WithDankookUser targetUser = withDankookUserRepository.findByUserIdAndWithDankookId(targetUserId, roommateId).orElseThrow(WithDankookUserNotFoundException::new);

        if (!checkIsMine(writerId, roommate)) {
            throw new NotGrantedException();
        }

        if (targetUser.getParticipantStatus().equals(ParticipantStatus.WAITING)
                && withDankookUserRepository.findByUserIdCheckingValid(roommateId, targetUserId).isEmpty()) {
            targetUser.changeStatusToValid();
            withDankookUserRepository.save(targetUser);
        } else {
            throw new NotGrantedException();
        }

        if (withDankookUserService.recruitedCount(roommateId) == 2) {
            roommate.markAsClosed();
        }
    }

    @Transactional
    public void delete(Long userId, boolean isAdmin, Long withDankookId) {
        withDankookService.delete(roommateRepository, withDankookId, userId, isAdmin);
        WithDankookUser withDankookUser = withDankookUserRepository.findByUserIdAndWithDankookId(userId, withDankookId).orElseThrow(WithDankookUserNotFoundException::new);
        withDankookUser.changeStatusToInvalid();
    }

    private void validateApplicant(Roommate roommate, User user) {
        boolean isInvalid = withDankookService.isInvalidParticipant(roommate, user);
        if(isInvalid) {
            throw new InvalidStatusException();
        }
        if (roommate.getMinStudentId() < Integer.parseInt(String.valueOf(user.getYearOfAdmission()).substring(2))) {
            throw new InvalidMinStudentIdException();
        }
        if (withDankookUserService.isParticipant(roommate.getId(), user.getId())) {
            throw new AlreadyEnteredException();
        }
    }


    private int checkEqualCount(RoomMateSurvey userSurvey, RoomMateSurvey writerSurvey) {
        int count = 0;
        if (userSurvey.getMbti().equals(writerSurvey.getMbti())) {
            count++;
        }
        if (userSurvey.isSleepHabit() == writerSurvey.isSleepHabit()) {
            count++;
        }
        if (userSurvey.isSleepSensitive() == writerSurvey.isSleepSensitive()) {
            count++;
        }
        if (userSurvey.isSmoking() == writerSurvey.isSmoking()) {
            count++;
        }
        if (userSurvey.isNoiseHabit() == writerSurvey.isNoiseHabit()) {
            count++;
        }
        if (userSurvey.getSleepTime().equals(writerSurvey.getSleepTime())) {
            count++;
        }
        if (userSurvey.getCleanUpCount() == writerSurvey.getCleanUpCount()) {
            count++;
        }
        return count;
    }

    private boolean checkIsMine(Long userId, Roommate roommate) {
        return roommate.getMasterUser().getId().equals(userId);
    }
}
