package com.dku.council.domain.post.controller;

import com.dku.council.domain.post.service.post.PostSearchService;
import com.dku.council.domain.post.service.post.SummarizedPostSearchDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "게시글 검색", description = "게시글 검색 관련 api")
@RequestMapping("/post/search")
public class PostSearchController {

    private final PostSearchService postSearchService;

    /**
     * 모든 게시글 검색
     *
     * @param keyword 검색어
     **/
    @GetMapping
    public SummarizedPostSearchDto searchPost(@RequestParam(required = false) String keyword,
                                              @ParameterObject Pageable pageable,
                                              int bodySize) {
        return postSearchService.searchPost(keyword, pageable, bodySize);
    }
}
