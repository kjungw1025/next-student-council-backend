package com.dku.council.domain.post.service.post;

import com.dku.council.domain.post.model.dto.response.ResponseSingleSearchPost;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SummarizedPostSearchDto {

    private final List<ResponseSingleSearchPost> notices;

    private final List<ResponseSingleSearchPost> coalitions;

    private final List<ResponseSingleSearchPost> petitions;

    private final List<ResponseSingleSearchPost> conferences;

    private final List<ResponseSingleSearchPost> rules;

}
