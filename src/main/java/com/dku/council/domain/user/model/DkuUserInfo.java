package com.dku.council.domain.user.model;

import com.dku.council.infra.dku.model.StudentInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class DkuUserInfo {
    private final String studentName;
    private final String studentId;
    private final String age;
    private final String gender;
    private final int yearOfAdmission;
    private final String studentState;

    private final String majorName;
    private final String departmentName;

    public DkuUserInfo(StudentInfo info) {
        this.studentName = info.getStudentName();
        this.studentId = info.getStudentId();
        this.age = info.getAge();
        this.gender = info.getGender();
        this.yearOfAdmission = info.getYearOfAdmission();
        this.studentState = info.getStudentState();
        this.majorName = info.getMajorName();
        this.departmentName = info.getDepartmentName();
    }
}
