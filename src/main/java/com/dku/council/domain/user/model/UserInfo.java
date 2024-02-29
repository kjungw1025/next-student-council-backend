package com.dku.council.domain.user.model;

import com.dku.council.domain.user.model.dto.response.ResponseScopedUserInfoDto;
import com.dku.council.domain.user.model.entity.Major;
import com.dku.council.domain.user.model.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class UserInfo {
    private final Long userId;
    private final String name;
    private final String nickname;
    private final String studentId;
    private final String phone;
    private final MajorInfo major;
    private final int yearOfAdmission;
    private final String academicStatus;
    private final String age;
    private final String gender;
    private final String profileImage;
    private final UserStatus status;

    public UserInfo(User user) {
        this.userId = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.studentId = user.getStudentId();
        this.phone = user.getPhone();
        this.major = new MajorInfo(user.getMajor());
        this.yearOfAdmission = user.getYearOfAdmission();
        this.academicStatus = user.getAcademicStatus();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.profileImage = user.getProfileImage();
        this.status = user.getStatus();
    }

    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    public static class MajorInfo {
        private final String name;
        private final String department;

        public MajorInfo(Major major) {
            this.name = major.getName();
            this.department = major.getDepartment();
        }
    }

    public ResponseScopedUserInfoDto getScopedInfo(Long userId, Set<String> scopes) {
        return ResponseScopedUserInfoDto.builder()
                .userId(userId)
                .studentId(scopes.contains("studentId") ? studentId : null)
                .username(scopes.contains("name") ? name : null)
                .nickname(scopes.contains("nickname") ? nickname : null)
                .phoneNumber(scopes.contains("phoneNumber") ? phone : null)
                .major(scopes.contains("major") ? major.getName() : null)
                .department(scopes.contains("major") ? major.getDepartment() : null)
                .age(scopes.contains("age") ? age : null)
                .gender(scopes.contains("gender") ? gender : null)
                .profileImage(scopes.contains("profile_image") ? profileImage : null)
                .build();
    }
}
