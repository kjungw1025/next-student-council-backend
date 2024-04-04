package com.dku.council.domain.danfesta.service;

import com.dku.council.domain.danfesta.exception.StampNotFoundException;
import com.dku.council.domain.danfesta.model.dto.response.ResponseStampForAdminDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseStampInfoDto;
import com.dku.council.domain.danfesta.model.entity.Stamp;
import com.dku.council.domain.danfesta.repository.StampRepository;
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
public class StampService {

    private final StampRepository stampRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createDefaultStampInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        Stamp s = Stamp.builder()
                .user(user)
                .build();
        stampRepository.save(s);
    }

    public ResponseStampInfoDto getStampInfo(Long userId) {
        Stamp s = stampRepository.findByUserId(userId).orElseThrow(StampNotFoundException::new);

        return new ResponseStampInfoDto(s);
    }

    public ResponseStampForAdminDto getStampInfoForAdmin(Long userId, String studentId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }

        Stamp s = stampRepository.findByStudentId(studentId).orElseThrow(StampNotFoundException::new);
        return new ResponseStampForAdminDto(s);
    }

    @Transactional
    public void stamp(Long userId, String studentId , int boothNumber) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getUserRole().isAdmin()) {
            throw new NotGrantedException();
        }

        String booth = "mission" + boothNumber;

        Stamp s = stampRepository.findByStudentId(studentId).orElseThrow(StampNotFoundException::new);

        s.stampToMission(booth);
    }
}
