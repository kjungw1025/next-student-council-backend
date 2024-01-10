package com.dku.council.domain.with_dankook.controller;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedEatingAloneDto;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedEatingAlonePossibleReviewDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateEatingAloneDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleEatingAloneDto;
import com.dku.council.domain.with_dankook.service.EatingAloneService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "단혼밥", description = "단혼밥 게시판")
@RequestMapping("with-dankook/eating-alone")
@RequiredArgsConstructor
public class EatingAloneController {

    private final EatingAloneService eatingAloneService;

    /**
     * 단혼밥 게시글 목록 조회
     *
     * @param bodySize    게시글 본문 길이. (글자 단위) 지정하지 않으면 50 글자.
     * @param pageable    페이징 size, sort, page
     * @return            페이징된 EatingAlone 게시판 목록
     */
    @GetMapping
    public ResponsePage<SummarizedEatingAloneDto> list(@RequestParam(defaultValue = "50") int bodySize,
                                                       @ParameterObject Pageable pageable) {
        Page<SummarizedEatingAloneDto> list = eatingAloneService.list(pageable, bodySize);
        return new ResponsePage<>(list);
    }

    /**
     * 내가 쓴 단혼밥 게시글 목록 조회
     *
     * @param pageable    페이징 size, sort, page
     * @return            페이징된 내가 쓴 EatingAlone 게시판 목록
     */
    @GetMapping("/my")
    @UserAuth
    public ResponsePage<SummarizedEatingAloneDto> listMyPosts(AppAuthentication auth,
                                                              @ParameterObject Pageable pageable) {
        Page<SummarizedEatingAloneDto> list = eatingAloneService.listMyPosts(auth.getUserId(), pageable);
        return new ResponsePage<>(list);
    }

    /**
     * 단혼밥 게시글 상세 조회
     *
     * @param id    게시글 id
     */
    @GetMapping("/{id}")
    @UserAuth
    public ResponseSingleEatingAloneDto findOne(AppAuthentication auth,
                                                @PathVariable Long id) {
        return eatingAloneService.findOne(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 단혼밥 게시글 생성
     */
    @PostMapping
    @UserAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @RequestBody RequestCreateEatingAloneDto dto) {
        Long id = eatingAloneService.create(auth.getUserId(), dto);
        return new ResponseIdDto(id);
    }

    /**
     * 단혼밥 게시글 참여
     *
     * @param id    게시글 id
     */
    @PostMapping("/{id}/enter")
    @UserAuth
    public void enter(AppAuthentication auth,
                       @PathVariable @Valid Long id) {
        eatingAloneService.enter(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 단혼밥 게시글 삭제
     *
     * @param id    게시글 id
     */
    @DeleteMapping("/{id}")
    @UserAuth
    public void delete(AppAuthentication auth,
                       @PathVariable @Valid Long id) {
        eatingAloneService.delete(id, auth.getUserId(), auth.getUserRole().isAdmin());
    }

    /**
     * 단혼밥 게시글 모집 완료 처리
     * 유저가 처리하거나 관리자가 강제로 처리할 수 있습니다.
     *
     * @param id   게시글 id
     */
    @PatchMapping("/{id}")
    @UserAuth
    public void close(AppAuthentication auth, @PathVariable Long id) {
        eatingAloneService.close(id, auth.getUserId());
    }

    /**
     * 내가 참여한 단혼밥 게시글 중, 리뷰 작성이 가능한 게시글 목록 조회
     *
     * @param pageable 페이징 size, sort, page
     * @return         페이징된 리뷰 작성이 가능한 단혼밥 게시글 목록 조회
     */
    @GetMapping("/my/possible/review")
    @UserAuth
    public ResponsePage<SummarizedEatingAlonePossibleReviewDto> listPossibleReviewPosts(AppAuthentication auth,
                                                                                        @ParameterObject Pageable pageable) {
        Page<SummarizedEatingAlonePossibleReviewDto> list = eatingAloneService.listMyPossibleReviewPosts(auth.getUserId(), pageable);
        return new ResponsePage<>(list);
    }
}
