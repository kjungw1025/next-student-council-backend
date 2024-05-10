package com.dku.council.domain.user.model.entity;

import com.dku.council.domain.user.model.UserStatus;
import com.dku.council.domain.user.service.UserInfoService;
import com.dku.council.global.auth.role.UserRole;
import com.dku.council.global.base.BaseEntity;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "dku_user",
        indexes = {
                @Index(name = "idx_user_student_id", columnList = "studentId"),
                @Index(name = "idx_user_phone", columnList = "phone"),
                @Index(name = "idx_user_nickname", columnList = "nickname")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    public static String ANONYMITY = "익명";
    public static String DELETED_USER = "탈퇴한 회원";

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column
    private String studentId;

    @NotNull
    private String password;

    @NotNull
    @Column(length = 20)
    private String name;

    @NotNull
    @Column(length = 20)
    private String nickname;

    private String age;

    private String gender;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "major_id")
    private Major major;

    private Integer yearOfAdmission;

    private String academicStatus;

    @NotNull
    @Column
    private String phone;

    @Lob
    private String profileImage;

    @Enumerated(STRING)
    private UserStatus status;

    @Enumerated(STRING)
    private UserRole userRole;

    @NotNull
    private boolean isDkuChecked;

    @Builder
    private User(@NonNull String studentId,
                 @NonNull String password,
                 @NonNull String name,
                 @NonNull Major major,
                 @NonNull String phone,
                 @NonNull String nickname,
                 @NonNull String academicStatus,
                 @NonNull String age,
                 @NonNull String gender,
                 Integer yearOfAdmission,
                 UserStatus status,
                 UserRole role) {
        this.studentId = studentId;
        this.password = password;
        this.name = name;
        this.major = major;
        this.phone = phone;
        this.nickname = nickname;
        this.academicStatus = academicStatus;
        this.age = age;
        this.gender = gender;
        this.yearOfAdmission = yearOfAdmission;
        this.status = status;
        this.userRole = role;
        this.isDkuChecked = true;
    }

    public String getName() {
        if (!getStatus().isActive()) {
            return DELETED_USER;
        }
        return this.name;
    }

    public String getNickname() {
        if (!getStatus().isActive()) {
            return DELETED_USER;
        }
        return this.nickname;
    }

    /**
     * 비밀번호를 변경합니다. {@link PasswordEncoder}로 인코딩된 비밀번호를 넣어야 합니다.
     *
     * @param encodedPassword 인코딩된 비밀번호
     */
    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    /**
     * 닉네임을 변경합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     *
     * @param nickname 닉네임
     */
    public void changeNickName(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 휴대폰 번호를 변경합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     *
     * @param phone 전화번호
     */
    public void changePhone(String phone) {
        this.phone = phone;
    }

    /**
     * User 상태를 변경합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     *
     * @param status 상태
     */
    public void changeStatus(UserStatus status) {
        this.status = status;
    }

    /**
     * 나이를 변경합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     *
     * @param age 나이
     */
    public void changeAge(String age) {
        this.age = age;
    }

    /**
     * 공통 User 정보를 수정합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     *
     * @param studentId       학번
     * @param studentName     이름
     * @param major           전공
     * @param yearOfAdmission 입학년도
     * @param studentState    학적상태
     */
    public void changeGenericInfo(String studentId, String studentName, Major major, int yearOfAdmission, String studentState, String age, String gender) {
        this.studentId = studentId;
        this.name = studentName;
        this.major = major;
        this.yearOfAdmission = yearOfAdmission;
        this.academicStatus = studentState;
        this.age = age;
        this.gender = gender;
    }

    /**
     * 탈퇴한 User의 정보을 수정합니다.
     * User정보 캐시를 삭제하기위해 {@link UserInfoService}.invalidateUserInfo를 호출해야 합니다.
     */
    public void emptyOutUserInfo() {
        this.studentId = "";
        this.name = "";
        this.phone = "";
        this.nickname = "";
        this.age = "";
        this.gender = "";
        this.password = "";
    }

    /**
     * 학생 인증을 갱신했을 경우 true로 변경해줍니다.
     */
    public void changeIsDkuChecked() {
        this.isDkuChecked = !this.isDkuChecked;
    }

    /**
     * 단국대 학생 정보를 업데이트합니다.
     *
     * @param age      나이
     * @param gender   성별
     */
    public void updateDkuInfo(String age, String gender) {
        this.isDkuChecked = true;
        this.age = age;
        this.gender = gender;
    }

    /**
     * 매년 1월 1일 사용자들의 나이 1을 증가시킵니다.
     */
    public void updateAge() {
        int age = Integer.parseInt(this.age);
        age++;
        this.age = String.valueOf(age);
    }
}
