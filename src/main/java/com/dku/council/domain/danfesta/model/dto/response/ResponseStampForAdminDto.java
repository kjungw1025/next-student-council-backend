package com.dku.council.domain.danfesta.model.dto.response;

import com.dku.council.domain.danfesta.model.entity.Stamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseStampForAdminDto {

    @Schema(description = "학번", example = "20180000")
    private final String studentId;

    @Schema(description = "이름", example = "홍길동")
    private final String studentName;

    @Schema(description = "미션1", example = "false")
    private final boolean mission1;

    @Schema(description = "미션2", example = "false")
    private final boolean mission2;

    @Schema(description = "미션3", example = "false")
    private final boolean mission3;

    @Schema(description = "미션4", example = "false")
    private final boolean mission4;

    @Schema(description = "미션5", example = "false")
    private final boolean mission5;

    @Schema(description = "미션6", example = "false")
    private final boolean mission6;

    @Schema(description = "미션7", example = "false")
    private final boolean mission7;

    public ResponseStampForAdminDto(Stamp s) {
        this.studentId = s.getUser().getStudentId();
        this.studentName = s.getUser().getName();
        this.mission1 = s.isMission1();
        this.mission2 = s.isMission2();
        this.mission3 = s.isMission3();
        this.mission4 = s.isMission4();
        this.mission5 = s.isMission5();
        this.mission6 = s.isMission6();
        this.mission7 = s.isMission7();
    }
}
