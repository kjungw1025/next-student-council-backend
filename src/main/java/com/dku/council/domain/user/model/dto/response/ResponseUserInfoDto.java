package com.dku.council.domain.user.model.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseUserInfoDto {

    private final String studentId;
    private final String username;
    private final String nickname;
    private final String age;
    private final String gender;
    private final String yearOfAdmission;
    private final String major;
    private final String department;
    private final String phoneNumber;
    private final String profileImage;
    private final Long writePostCount;
    private final Long commentedPostCount;
    private final Long likedPostCount;
    private final Long petitionCount;
    private final Long agreedPetitionCount;
    private final boolean admin;
}
