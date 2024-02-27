package com.dku.council.domain.user.service;

import com.dku.council.domain.comment.repository.CommentRepository;
import com.dku.council.domain.like.model.LikeTarget;
import com.dku.council.domain.like.service.LikeService;
import com.dku.council.domain.post.repository.post.PostRepository;
import com.dku.council.domain.user.model.UserInfo;
import com.dku.council.domain.user.model.dto.response.ResponseUserInfoDto;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.user.repository.UserInfoMemoryRepository;
import com.dku.council.domain.user.repository.UserRepository;
import com.dku.council.global.error.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Clock;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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
                major.getName(), major.getDepartment(), phoneNumber, user.getProfileImage(),
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
    public Map<String, Object> getScopedUserInfo(Long userId, String scope) {
        String[] scopes = scope.split(" ");
        UserInfo userInfo = getUserInfo(userId);

        Map<String, Object> scopedInfo = getScopedInfo(scopes, userInfo);
        scopedInfo.put("userId", userId);
        return scopedInfo;
    }

    @NotNull
    private static Map<String, Object> getScopedInfo(String[] scopes, UserInfo userInfo) {
        return Arrays.stream(scopes)
                .collect(Collectors.toMap(
                        Function.identity(),
                        s -> {
                            try {
                                String methodName = "get" + s.substring(0, 1).toUpperCase() + s.substring(1);
                                Method method = UserInfo.class.getMethod(methodName);
                                return method.invoke(userInfo);
                            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                ));
    }

    public void invalidateUserInfo(Long userId) {
        memoryRepository.removeUserInfo(userId);
    }

    public void cacheUserInfo(Long userId, User user) {
        UserInfo userInfo = new UserInfo(user);
        memoryRepository.setUserInfo(userId, userInfo, Instant.now(clock));
    }
}
