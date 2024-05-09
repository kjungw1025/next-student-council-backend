package com.dku.council.domain.danfesta.model.dto.response;

import com.dku.council.domain.danfesta.model.entity.Stamp;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseStampInfoDto {

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

    public ResponseStampInfoDto(Stamp stamp) {
        this.mission1 = stamp.isMission1();
        this.mission2 = stamp.isMission2();
        this.mission3 = stamp.isMission3();
        this.mission4 = stamp.isMission4();
        this.mission5 = stamp.isMission5();
        this.mission6 = stamp.isMission6();
        this.mission7 = stamp.isMission7();
    }
}
