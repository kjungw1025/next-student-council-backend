package com.dku.council.domain.user.service;

import com.dku.council.domain.user.exception.AlreadyNicknameException;
import com.dku.council.domain.user.exception.DkuAuthNotRefreshedException;
import com.dku.council.domain.user.exception.RequiredDkuUpdateException;
import com.dku.council.domain.user.exception.WrongPasswordException;
import com.dku.council.domain.user.model.UserStatus;
import com.dku.council.domain.user.model.dto.request.RequestExistPasswordChangeDto;
import com.dku.council.domain.user.model.dto.request.RequestLoginDto;
import com.dku.council.domain.user.model.dto.request.RequestNickNameChangeDto;
import com.dku.council.domain.user.model.dto.response.*;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.MajorRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.auth.jwt.AuthenticationToken;
import com.dku.council.global.auth.jwt.JwtProvider;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final UserInfoService userInfoService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public ResponseLoginDto login(RequestLoginDto dto) {
        User user = userRepository.findByStudentId(dto.getStudentId())
                .orElseThrow(UserNotFoundException::new);

        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            AuthenticationToken token = jwtProvider.issue(user);
            userInfoService.cacheUserInfo(user.getId(), user);
            return new ResponseLoginDto(token);
        } else {
            throw new WrongPasswordException();
        }
    }

    public ResponseRefreshTokenDto refreshToken(HttpServletRequest request, String refreshToken) {
        String accessToken = jwtProvider.getAccessTokenFromHeader(request);
        AuthenticationToken token = jwtProvider.reissue(accessToken, refreshToken);
        return new ResponseRefreshTokenDto(token);
    }

    public List<ResponseMajorDto> getAllMajors() {
        return majorRepository.findAll().stream()
                .map(ResponseMajorDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeNickName(Long userId, RequestNickNameChangeDto dto) {
        User user = findUser(userId);
        checkAlreadyNickname(dto.getNickname());
        user.changeNickName(dto.getNickname());
        userInfoService.invalidateUserInfo(userId);
    }

    @Transactional
    public void changePassword(Long userId, RequestExistPasswordChangeDto dto) {
        User user = findUser(userId);
        if (passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
            user.changePassword(encodedPassword);
            userInfoService.invalidateUserInfo(userId);
        } else {
            throw new WrongPasswordException();
        }
    }

    @Transactional
    public void activateUser(Long userId) {
        User user = findUser(userId);
        user.changeStatus(UserStatus.ACTIVE);
        userInfoService.invalidateUserInfo(userId);
    }

    @Transactional
    public void deactivateUser(Long userId) {
        User user = findUser(userId);
        user.changeStatus(UserStatus.INACTIVE);
        userInfoService.invalidateUserInfo(userId);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    public void checkAlreadyNickname(String nickname) {
        if (userRepository.findByNickname(nickname).isPresent()) {
            throw new AlreadyNicknameException();
        }
    }

    /**
     *  단국대학교 학생 인증을 확인하기 위한 메소드이다.
     *
     * @UserAuth 어노테이션을 사용하는 모든 메소드에 필수적으로 사용해야 한다.
     */
    public void isDkuChecked(Long userId) {
        User user = findUser(userId);
        if (!user.isDkuChecked()) {
            throw new RequiredDkuUpdateException();
        }
    }

    public ResponseUserInfoForChattingDto getUserInfoForChatting(Long memberId) {
        User user = userRepository.findById(memberId).orElseThrow(UserNotFoundException::new);
        return new ResponseUserInfoForChattingDto(user.getStudentId(), user.getName(), user.getNickname(), user.getUserRole().isAdmin());
    }
}
