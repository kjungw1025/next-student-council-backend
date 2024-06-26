package com.dku.council.domain.user.service;

import com.dku.council.domain.comment.repository.CommentRepository;
import com.dku.council.domain.like.model.LikeTarget;
import com.dku.council.domain.like.service.LikeService;
import com.dku.council.domain.post.repository.post.PostRepository;
import com.dku.council.domain.user.model.UserInfo;
import com.dku.council.domain.user.model.dto.response.ResponseScopedUserInfoDto;
import com.dku.council.domain.user.model.dto.response.ResponseUserInfoDto;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserInfoMemoryRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserInfoService {

    private final Clock clock;
    private final UserRepository persistenceRepository;
    private final UserInfoMemoryRepository memoryRepository;
    private final CommentRepository commentRepository;
    private final LikeService likeService;
    private final PostRepository postRepository;


    @Transactional
    public ResponseUserInfoDto getFullUserInfo(Long userId) {
        User user = persistenceRepository.findByIdWithMajor(userId).orElseThrow(UserNotFoundException::new);

        String year = user.getYearOfAdmission().toString();
        Major major = user.getMajor();
        String phoneNumber = user.getPhone();

        Long writePostCount = postRepository.countAllByUserId(userId);
        Long commentedPostCount = commentRepository.countAllCommentedByUserId(userId);
        Long likedPostCount = likeService.getCountOfLikedElements(userId, LikeTarget.POST);
        Long petitionCount = postRepository.countAllPetitionByUserId(userId);
        Long agreedPetitionCount = postRepository.countAllAgreedPetitionByUserId(userId);

        return new ResponseUserInfoDto(user.getStudentId(), user.getName(),
                user.getNickname(), user.getAge(), user.getGender(), year,
                major.getName(), major.getDepartment(), phoneNumber,
                writePostCount, commentedPostCount, likedPostCount,
                petitionCount, agreedPetitionCount, user.isDkuChecked(), user.getUserRole().isAdmin());
    }

    @Transactional(readOnly = true)
    public UserInfo getUserInfo(Long userId) {
        Instant now = Instant.now(clock);
        return memoryRepository.getUserInfo(userId, now)
                .orElseGet(() -> {
                    User user = persistenceRepository.findByIdWithMajor(userId)
                            .orElseThrow(UserNotFoundException::new);
                    UserInfo userInfo = new UserInfo(user);
                    memoryRepository.setUserInfo(userId, userInfo, now);
                    return userInfo;
                });
    }

    @Transactional(readOnly = true)
    public ResponseScopedUserInfoDto getScopedUserInfo(Long userId, String scope) {
        UserInfo userInfo = getUserInfo(userId);
        return userInfo.getScopedInfo(userId, getScopeSet(scope));
    }

    private Set<String> getScopeSet(String scope) {
        return new HashSet<>(Arrays.asList(scope.split(" ")));
    }

    public void invalidateUserInfo(Long userId) {
        memoryRepository.removeUserInfo(userId);
    }

    public void cacheUserInfo(Long userId, User user) {
        UserInfo userInfo = new UserInfo(user);
        memoryRepository.setUserInfo(userId, userInfo, Instant.now(clock));
    }
}
