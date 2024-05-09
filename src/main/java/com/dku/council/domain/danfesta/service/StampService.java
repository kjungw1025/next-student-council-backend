package com.dku.council.domain.danfesta.service;

import com.dku.council.domain.danfesta.exception.StampNotFoundException;
import com.dku.council.domain.danfesta.model.dto.response.ResponseStampInfoDto;
import com.dku.council.domain.danfesta.model.entity.Stamp;
import com.dku.council.domain.danfesta.repository.StampRepository;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
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
    public ResponseStampInfoDto getStampInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Stamp s;

        if(stampRepository.findByUserId(userId).isEmpty()) {
            s = Stamp.builder()
                    .user(user)
                    .build();
            s = stampRepository.save(s);
        } else {
            s = stampRepository.findByUserId(userId).orElseThrow(StampNotFoundException::new);
        }

        return new ResponseStampInfoDto(s);
    }
}
