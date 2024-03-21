package com.dku.council.domain.danfesta.model.dto.response;

import com.dku.council.domain.danfesta.model.entity.SpecialGuest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseSpecialMissionInfoDto {

    @Schema(description = "첫 번째 부스 미션", example = "false")
    private final boolean mission1;

    @Schema(description = "두 번째 부스 미션", example = "false")
    private final boolean mission2;

    @Schema(description = "세 번째 부스 미션", example = "false")
    private final boolean mission3;

    @Schema(description = "네 번째 부스 미션", example = "false")
    private final boolean mission4;

    @Schema(description = "다섯 번째 부스 미션", example = "false")
    private final boolean mission5;

    @Schema(description = "여섯 번째 부스 미션", example = "false")
    private final boolean mission6;

    @Schema(description = "일곱 번째 부스 미션", example = "false")
    private final boolean mission7;

    public ResponseSpecialMissionInfoDto(SpecialGuest specialGuest) {
        this.mission1 = specialGuest.isMission1();
        this.mission2 = specialGuest.isMission2();
        this.mission3 = specialGuest.isMission3();
        this.mission4 = specialGuest.isMission4();
        this.mission5 = specialGuest.isMission5();
        this.mission6 = specialGuest.isMission6();
        this.mission7 = specialGuest.isMission7();
    }
}
