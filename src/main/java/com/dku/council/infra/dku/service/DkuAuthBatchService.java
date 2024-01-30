package com.dku.council.infra.dku.service;

import com.dku.council.domain.user.exception.FailedAuthRefreshException;
import com.dku.council.domain.user.model.DkuUserInfo;
import com.dku.council.domain.user.model.dto.request.RequestDkuStudentDto;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import com.dku.council.infra.dku.model.DkuAuth;
import com.dku.council.infra.dku.model.StudentInfo;
import com.dku.council.infra.dku.scrapper.DkuAuthenticationService;
import com.dku.council.infra.dku.scrapper.DkuStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DkuAuthBatchService {

    private final UserRepository userRepository;
    private final DkuStudentService crawlerService;
    private final DkuAuthenticationService authenticationService;

    /**
     *  저장되어 있는 학생들의 계정 인증을 false로 변경.
     */
    @Transactional
    public void resetDkuAuth() {
        List<User> listUser = userRepository.findAllWithDkuChecked();
        for (User user : listUser) {
            user.changeIsDkuChecked();
        }
    }

    /**
     *  학생 계정 인증 갱신 과정 수행.
     */
    @Transactional
    public void changeDkuAuth(RequestDkuStudentDto dto) {
        DkuUserInfo info = retrieveDkuUserInfoForRefresh(dto.getDkuStudentId(), dto.getDkuPassword());
        if (info.getStudentState().equals("재학")) {
            User user = userRepository.findByStudentId(dto.getDkuStudentId())
                    .orElseThrow(UserNotFoundException::new);
            user.updateDkuInfo(info.getAge(), info.getGender(), info.getProfileImageUrl());
        }
        else {
            throw new FailedAuthRefreshException();
        }
    }

    private DkuUserInfo retrieveDkuUserInfoForRefresh(String id, String pwd) {
        DkuAuth auth = authenticationService.loginWebInfo(id, pwd);
        StudentInfo studentInfo = crawlerService.crawlStudentInfo(auth);
        return new DkuUserInfo(studentInfo);
    }

}
