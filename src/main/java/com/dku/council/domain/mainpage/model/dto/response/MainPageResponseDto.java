package com.dku.council.domain.mainpage.model.dto.response;

import com.dku.council.domain.mainpage.model.dto.PetitionSummary;
import com.dku.council.domain.mainpage.model.dto.PostSummary;
import lombok.Getter;

import java.util.List;

@Getter
public class MainPageResponseDto {
    private final List<CarouselImageResponse> carousels;
    private final List<PostSummary> recentNotices;
    private final List<PostSummary> recentConferences;
    private final List<PetitionSummary> popularPetitions;


    public MainPageResponseDto(List<CarouselImageResponse> carousels, List<PostSummary> recentNotices, List<PostSummary> recentConferences, List<PetitionSummary> popularPetitions) {
        this.carousels = carousels;
        this.recentNotices = recentNotices;
        this.recentConferences = recentConferences;
        this.popularPetitions = popularPetitions;
    }

}
