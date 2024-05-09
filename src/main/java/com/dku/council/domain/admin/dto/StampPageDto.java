package com.dku.council.domain.admin.dto;

import com.dku.council.domain.danfesta.model.entity.Stamp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StampPageDto {

    private final Long id;
    private final String name;
    private final String studentId;
    private final boolean mission1;
    private final boolean mission2;
    private final boolean mission3;
    private final boolean mission4;
    private final boolean mission5;
    private final boolean mission6;
    private final boolean mission7;

    public StampPageDto(Stamp stamp) {
        this.id = stamp.getId();
        this.name = stamp.getUser().getName();
        this.studentId = stamp.getUser().getStudentId();
        this.mission1 = stamp.isMission1();
        this.mission2 = stamp.isMission2();
        this.mission3 = stamp.isMission3();
        this.mission4 = stamp.isMission4();
        this.mission5 = stamp.isMission5();
        this.mission6 = stamp.isMission6();
        this.mission7 = stamp.isMission7();
    }
}
