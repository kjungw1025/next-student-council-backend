package com.dku.council.domain.with_dankook.controller;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.user.service.UserService;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedStudyDto;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedStudyPossibleReviewDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateStudyDto;
import com.dku.council.domain.with_dankook.model.dto.response.ResponseSingleStudyDto;
import com.dku.council.domain.with_dankook.service.StudyService;
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

@Tag(name = "단터디 게시판", description = "단터디 게시판 API")
@RestController
@RequestMapping("with-dankook/study")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;
    private final UserService userService;

    /**
     * 단터디 게시글 목록 조회
     *
     * @param keyword 제목이나 내용에 포함된 검색어. 지정하지 않으면 모든 게시글 조회.
     * @param bodySize 게시글 본문 길이. (글자 단위) 지정하지 않으면 50 글자.
     * @param pageable 페이징 size, sort, page
     * @return 페이징된 단터디 게시판 목록
     */
    @GetMapping
    public ResponsePage<SummarizedStudyDto> list(@RequestParam(required = false) String keyword,
                                                 @RequestParam(defaultValue = "50") int bodySize,
                                                 @ParameterObject Pageable pageable) {
        Page<SummarizedStudyDto> list = studyService.list(keyword, pageable, bodySize);
        return new ResponsePage<>(list);
    }

    /**
     * 태그를 통한 단터디 게시글 목록 조회
     *
     * @param studyTagName 태그 이름. 지정하지 않으면 모든 게시글 조회.
     * @param bodySize 게시글 본문 길이. (글자 단위) 지정하지 않으면 50 글자.
     * @param pageable 페이징 size, sort, page
     * @return 페이징된 단터디 게시판 목록
     */
    @GetMapping("/tag")
    public ResponsePage<SummarizedStudyDto> listByStudyTag(@RequestParam(required = false) String studyTagName,
                                                           @RequestParam(defaultValue = "50") int bodySize,
                                                           @ParameterObject Pageable pageable) {
        Page<SummarizedStudyDto> list = studyService.listByStudyTag(studyTagName, pageable, bodySize);
        return new ResponsePage<>(list);
    }

    /**
     * 내가 작성한 단터디 게시글 목록 조회
     *
     * @param pageable 페이징 size, sort, page
     * @return         페이징된 내가 쓴 단터디 게시판 목록
     */
    @GetMapping("/my")
    @UserAuth
    public ResponsePage<SummarizedStudyDto> listMyPosts(AppAuthentication auth,
                                                        @ParameterObject Pageable pageable) {
        userService.isDkuChecked(auth.getUserId());
        Page<SummarizedStudyDto> list = studyService.listMyPosts(auth.getUserId(), pageable);
        return new ResponsePage<>(list);
    }

    /**
     * 단터디 게시글 상세 조회
     *
     */
    @GetMapping("/{id}")
    @UserAuth
    public ResponseSingleStudyDto findOne(AppAuthentication auth,
                                          @PathVariable Long id) {
        userService.isDkuChecked(auth.getUserId());
        return studyService.findOne(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 단터디 게시글 등록
     */
    @PostMapping
    @UserAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @RequestBody RequestCreateStudyDto dto) {
        userService.isDkuChecked(auth.getUserId());
        Long id = studyService.create(auth.getUserId(), dto);
        return new ResponseIdDto(id);
    }

    /**
     * 단터디 게시글 신청
     *
     * @param id   게시글 id
     */
    @PostMapping("/{id}/enter")
    @UserAuth
    public void enter(AppAuthentication auth, @PathVariable @Valid Long id) {
        userService.isDkuChecked(auth.getUserId());
        studyService.enter(id, auth.getUserId(), auth.getUserRole());
    }

    /**
     * 단터디 게시글 삭제
     *
     * @param auth 사용자 인증정보
     * @param id   삭제할 게시글 id
     */
    @DeleteMapping("/{id}")
    @UserAuth
    public void delete(AppAuthentication auth,
                       @PathVariable Long id) {
        userService.isDkuChecked(auth.getUserId());
        studyService.delete(id, auth.getUserId(), auth.isAdmin());
    }

    /**
     * 단터디 게시글 모집 완료 처리
     * 유저가 처리하거나 관리자가 강제로 처리할 수 있습니다.
     *
     * @param id   게시글 id
     */
    @PatchMapping("/{id}")
    @UserAuth
    public void close(AppAuthentication auth, @PathVariable Long id) {
        userService.isDkuChecked(auth.getUserId());
        studyService.close(id, auth.getUserId());
    }

    /**
     * 내가 참여한 단터디 게시글 중, 리뷰 작성이 가능한 게시글 목록 조회
     *
     * @param pageable 페이징 size, sort, page
     * @return         페이징된 리뷰 작성이 가능한 단터디 게시글 목록 조회
     */
    @GetMapping("/my/possible/review")
    @UserAuth
    public ResponsePage<SummarizedStudyPossibleReviewDto> listPossibleReviewPosts(AppAuthentication auth,
                                                                                  @ParameterObject Pageable pageable) {
        userService.isDkuChecked(auth.getUserId());
        Page<SummarizedStudyPossibleReviewDto> list = studyService.listMyPossibleReviewPosts(auth.getUserId(), pageable);
        return new ResponsePage<>(list);
    }
}
