package com.dku.council.domain.batch;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DkuStudentAgeScheduler {

    private final UserRepository userRepository;

    // 매년 0시 0분 0초에 실행

    @Scheduled(cron = "0 0 0 1 1 *")
    public void dkuStudentAgeScheduler() {
        userRepository.findAll().forEach(User::updateAge);
    }
}
