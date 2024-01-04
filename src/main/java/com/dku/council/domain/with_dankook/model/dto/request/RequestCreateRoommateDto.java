package com.dku.council.domain.with_dankook.model.dto.request;

import com.dku.council.domain.user.model.entity.User;
import com.dku.council.domain.with_dankook.model.entity.ResidenceDuration;
import com.dku.council.domain.with_dankook.model.entity.type.Roommate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class RequestCreateRoommateDto extends RequestCreateWithDankookDto<Roommate> {

    @NotNull
    @Schema(description = "제목", example = "기숙사 구해요")
    private final String title;

    @NotNull
    @Schema(description = "최소 학번", example = "20190000")
    private final int minStudentId;

    @NotNull
    @Schema(description = "기숙사", example = "웅비홀")
    private final String livingPlace;

    @NotNull
    @Schema(description = "거주 기간", example = "HALF_YEAR")
    private final ResidenceDuration residenceDuration;

    public RequestCreateRoommateDto(@NotNull String title,
                                    @NotNull int minStudentId,
                                    @NotNull String livingPlace,
                                    @NotNull ResidenceDuration residenceDuration) {
        this.title = title;
        this.minStudentId = minStudentId;
        this.livingPlace = livingPlace;
        this.residenceDuration = residenceDuration;
    }

    @Override
    public Roommate toEntity(User user) {
        return Roommate.builder()
                .user(user)
                .title(title)
                .minStudentId(minStudentId)
                .livingPlace(livingPlace)
                .residenceDuration(residenceDuration)
                .build();
    }
}
