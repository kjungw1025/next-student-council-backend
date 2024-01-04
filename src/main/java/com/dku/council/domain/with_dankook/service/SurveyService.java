package com.dku.council.domain.with_dankook.service;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.domain.with_dankook.exception.AlreadySurveyException;
import com.dku.council.domain.with_dankook.exception.InvalidMBTIException;
import com.dku.council.domain.with_dankook.exception.InvalidNoiseHabitException;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateSurveyDto;
import com.dku.council.domain.with_dankook.repository.RoomMateSurveyRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

    private static final List<String> MBTIList = List.of(
            "ISTJ", "ISFJ", "INFJ", "INTJ", "ISTP", "ISFP", "INFP", "INTP",
            "ESTP", "ESFP", "ENFP", "ENTP", "ESTJ", "ESFJ", "ENFJ", "ENTJ");

    private final RoomMateSurveyRepository surveyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createSurvey(Long userId, RequestCreateSurveyDto dto) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (surveyRepository.existsByUserId(userId)) {
            throw new AlreadySurveyException();

        } else if (!isValidMBTI(dto.getMbti())) {
            throw new InvalidMBTIException();

        } else if (!checkSurveyNoiseHabit(dto.isNoiseHabit(), dto.getNoiseHabitDetail())) {
            throw new InvalidNoiseHabitException();

        } else {
            return surveyRepository.save(dto.toEntity(user)).getId();
        }
    }

    @Transactional(readOnly = true)
    public boolean checkSurvey(Long userId) {
        return surveyRepository.existsByUserId(userId);
    }

    private boolean isValidMBTI(String mbti) {
        return mbti.length() == 4 && MBTIList.contains(mbti);
    }

    private boolean checkSurveyNoiseHabit(boolean noiseHabit, String noiseHabitDetail) {
        return !noiseHabit || noiseHabitDetail != null;
    }
}
