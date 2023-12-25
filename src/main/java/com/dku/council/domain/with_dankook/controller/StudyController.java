package com.dku.council.domain.with_dankook.controller;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedStudyDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateStudyDto;
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
     * 단터디 게시글 등록
     */
    @PostMapping
    @UserAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @RequestBody RequestCreateStudyDto dto) {
        Long id = studyService.create(auth.getUserId(), dto);
        return new ResponseIdDto(id);
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
        studyService.delete(id, auth.getUserId(), auth.isAdmin());
    }
}
