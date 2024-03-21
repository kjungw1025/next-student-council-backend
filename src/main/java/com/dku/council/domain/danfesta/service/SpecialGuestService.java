package com.dku.council.domain.danfesta.service;

import com.dku.council.domain.danfesta.exception.SpecialMissionNotFoundException;
import com.dku.council.domain.danfesta.model.dto.response.ResponseSpecialMissionForAdminDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseSpecialMissionInfoDto;
import com.dku.council.domain.danfesta.model.entity.SpecialGuest;
import com.dku.council.domain.danfesta.repository.SpecialGuestRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.error.exception.NotGrantedException;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecialGuestService {

    private final SpecialGuestRepository specialGuestRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createDefaultSpecialMission(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        SpecialGuest sg = SpecialGuest.builder()
                .user(user)
                .build();
        specialGuestRepository.save(sg);
    }

    public ResponseSpecialMissionInfoDto getSpecialMissionInfo(Long userId) {
        SpecialGuest sg = specialGuestRepository.findByUserId(userId).orElseThrow(SpecialMissionNotFoundException::new);

        return new ResponseSpecialMissionInfoDto(sg);
    }

    public ResponseSpecialMissionForAdminDto getSpecialMissionInfoForAdmin(Long userId, String studentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }

        SpecialGuest sg = specialGuestRepository.findByStudentId(studentId).orElseThrow(SpecialMissionNotFoundException::new);
        return new ResponseSpecialMissionForAdminDto(sg);
    }

    @Transactional
    public void stampSpecialMission(Long userId, String studentId ,int boothNumber) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }

        String booth = "mission" + boothNumber;

        SpecialGuest sg = specialGuestRepository.findByStudentId(studentId).orElseThrow(SpecialMissionNotFoundException::new);

        sg.changeMissionStatus(booth);
    }
}
