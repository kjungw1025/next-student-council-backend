package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.studytag.model.entity.StudyTag;
import com.dku.council.domain.studytag.repository.StudyTagRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.InvalidMinStudentIdException;
import com.dku.council.domain.with_dankook.exception.StudyCooltimeException;
import com.dku.council.domain.with_dankook.exception.WithDankookNotFoundException;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedStudyDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateStudyDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleStudyDto;
import com.dku.council.domain.with_dankook.model.entity.WithDankookUser;
import com.dku.council.domain.with_dankook.model.entity.type.Study;
import com.dku.council.domain.with_dankook.repository.with_dankook.StudyRepository;
import com.dku.council.domain.with_dankook.repository.with_dankook.WithDankookMemoryRepository;
import com.dku.council.domain.with_dankook.repository.WithDankookUserRepository;
import com.dku.council.domain.with_dankook.repository.spec.WithDankookSpec;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StudyService {

    public static final String STUDY_KEY = "study";

    private final StudyRepository studyRepository;
    private final WithDankookMemoryRepository withDankookMemoryRepository;
    private final UserRepository userRepository;
    private final StudyTagRepository studyTagRepository;
    private final WithDankookUserRepository withDankookUserRepository;

    private final WithDankookService<Study> withDankookService;
    private final WithDankookUserService withDankookUserService;

    private final Clock clock;

    @Value("${app.with-dankook.study.write-cooltime}")
    private final Duration writeCooltime;

    @Transactional
    public Long create(Long userId, RequestCreateStudyDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Instant now = Instant.now(clock);
        if (withDankookMemoryRepository.isAlreadyContains(STUDY_KEY, userId, now)) {
            throw new StudyCooltimeException("study");
        }

        StudyTag studyTag = retrieveStudyTag(dto.getTag());

        Study study = Study.builder()
                .user(user)
                .title(dto.getTitle())
                .minStudentId(dto.getMinStudentId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .tag(studyTag)
                .content(dto.getContent())
                .build();

        Long result = studyRepository.save(study).getId();

        WithDankookUser withDankookUser = WithDankookUser.builder()
                .user(user)
                .withDankook(study)
                .build();
        withDankookUserRepository.save(withDankookUser);

        withDankookMemoryRepository.put(STUDY_KEY, userId, writeCooltime, now);
        return result;
    }

    private StudyTag retrieveStudyTag (String tagName) {
        return studyTagRepository.findByName(tagName)
                .orElseGet(() -> {
                    StudyTag entity = new StudyTag(tagName);
                    entity = studyTagRepository.save(entity);
                    return entity;
                });
    }

    @Transactional(readOnly = true)
    public Page<SummarizedStudyDto> list(String keyword, Pageable pageable, int bodySize) {
        Specification<Study> spec = WithDankookSpec.withTitleOrBody(keyword);
        spec = spec.and(WithDankookSpec.withActive());
        Page<Study> result = studyRepository.findAll(spec, pageable);
        return result.map((study) ->
                new SummarizedStudyDto(withDankookService.makeListDto(bodySize, study),
                                        study,
                                        withDankookUserService.recruitedCount(withDankookService.makeListDto(bodySize, study).getId())
                ));
    }

    @Transactional(readOnly = true)
    public Page<SummarizedStudyDto> listByStudyTag(String studyTagName, Pageable pageable, int bodySize) {
        Specification<Study> spec = WithDankookSpec.withStudyTagName(studyTagName);
        spec = spec.and(WithDankookSpec.withActive());
        Page<Study> result = studyRepository.findAll(spec, pageable);
        return result.map((study) ->
                new SummarizedStudyDto(withDankookService.makeListDto(bodySize, study),
                        study,
                        withDankookUserService.recruitedCount(withDankookService.makeListDto(bodySize, study).getId())
                ));
    }

    @Transactional(readOnly = true)
    public Page<SummarizedStudyDto> listMyPosts(Long userId, Pageable pageable) {
        return studyRepository.findAllStudyByUserId(userId, pageable)
                .map(study -> new SummarizedStudyDto(withDankookService.makeListDto(50, study),
                        study,
                        withDankookUserService.recruitedCount(withDankookService.makeListDto(50, study).getId())
                ));
    }

    @Transactional(readOnly = true)
    public ResponseSingleStudyDto findOne(Long studyId, Long userId, UserRole role) {
        Study study = findStudy(studyRepository, studyId, role);
        return new ResponseSingleStudyDto(withDankookService.makeSingleDto(userId, study),
                study,
                withDankookUserService.recruitedCount(withDankookService.makeSingleDto(userId, study).getId()));
    }

    private Study findStudy(StudyRepository studyRepository, Long studyId, UserRole role) {
        Optional<Study> study;
        if (role.isAdmin()) {
            study = studyRepository.findWithAllStatusById(studyId);
        } else {
            study = studyRepository.findWithNotDeletedById(studyId);
        }
        return study.orElseThrow(WithDankookNotFoundException::new);
    }

    @Transactional
    public void enter(Long id, Long userId, UserRole userRole) {
        Study study = findStudy(studyRepository, id, userRole);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (study.getMinStudentId() < Integer.parseInt(String.valueOf(user.getYearOfAdmission()).substring(2))) {
            throw new InvalidMinStudentIdException();
        } else {
            withDankookService.enter(studyRepository, id, userId, userRole);
        }
    }

    @Transactional
    public void delete(Long studyId, Long userId, boolean isAdmin) {
        withDankookService.delete(studyRepository, studyId, userId, isAdmin);
    }

    @Transactional
    public void close(Long tradeId, Long userId) {
        studyRepository.findById(tradeId).ifPresent(study -> {
            if (study.getMasterUser().getId().equals(userId)) {
                study.close();
            } else{
                throw new NotGrantedException();
            }
        });
    }
}
