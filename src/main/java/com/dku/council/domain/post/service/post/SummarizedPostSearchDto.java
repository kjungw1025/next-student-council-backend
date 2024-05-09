package com.dku.council.domain.post.service.post;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.post.model.dto.response.ResponseSingleSearchPost;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class SummarizedPostSearchDto {

    private final ResponsePage<ResponseSingleSearchPost> notices;

    private final ResponsePage<ResponseSingleSearchPost> coalitions;

    private final ResponsePage<ResponseSingleSearchPost> petitions;

}
