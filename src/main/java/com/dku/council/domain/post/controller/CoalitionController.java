package com.dku.council.domain.post.controller;

import com.dku.council.domain.like.model.LikeTarget;
import com.dku.council.domain.like.service.LikeService;
import com.dku.council.domain.post.model.CoalitionType;
import com.dku.council.domain.post.model.dto.list.SummarizedCoalitionDto;
import com.dku.council.domain.post.model.dto.request.RequestCreateCoalitionDto;
import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.post.model.dto.response.ResponseSingleGenericPostDto;
import com.dku.council.domain.post.service.post.CoalitionService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.AdminAuth;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.global.model.dto.ResponseIdDto;
import com.dku.council.global.util.RemoteAddressUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.dku.council.domain.like.model.LikeTarget.*;

@Tag(name = "제휴사업 게시판", description = "제휴사업 게시판 관련 api")
@RestController
@RequestMapping("/post/coalition")
@RequiredArgsConstructor
public class CoalitionController {

    private final CoalitionService service;
    private final LikeService likeService;

    /**
     * 게시글 목록으로 조회
     *
     * @param keyword  제목이나 내용에 포함된 검색어. 지정하지 않으면 모든 게시글 조회.
     * @param bodySize 게시글 본문 길이. (글자 단위) 지정하지 않으면 50 글자.
     * @param pageable 페이징 size, sort, page
     * @param coalitionType 조회할 제휴사업 분류 타입.
     * @return 페이징 된 제휴사업 목록
     */
    @GetMapping
    public ResponsePage<SummarizedCoalitionDto> list(@RequestParam(required = false) String keyword,
                                                     @RequestParam(defaultValue = "50") int bodySize,
                                                     @ParameterObject Pageable pageable,
                                                     @RequestParam CoalitionType coalitionType) {
        Page<SummarizedCoalitionDto> list = service.list(keyword, pageable, bodySize, coalitionType);
        return new ResponsePage<>(list);
    }

    /**
     * 게시글 등록 (Admin)
     *
     * @return 생성된 게시글 id
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminAuth
    public ResponseIdDto create(AppAuthentication auth, @Valid @ModelAttribute RequestCreateCoalitionDto request) {
        Long postId = service.create(auth.getUserId(), request);
        return new ResponseIdDto(postId);
    }

    /**
     * 게시글 단건 조회
     *
     * @param id 조회할 게시글 id
     * @return 제휴사업 게시글 정보
     */
    @PostMapping("{id}")
    @UserAuth
    public ResponseSingleGenericPostDto findOne(AppAuthentication auth,
                                                @PathVariable Long id,
                                                HttpServletRequest request) {
        return service.findOne(id, auth.getUserId(), auth.getUserRole(),
                RemoteAddressUtil.getProxyableAddr(request));
    }

    /**
     * 게시글 삭제
     *
     * @param id 삭제할 게시글 id
     */
    @DeleteMapping("/{id}")
    @AdminAuth
    public void delete(AppAuthentication auth, @PathVariable Long id) {
        service.delete(id, auth.getUserId(), auth.isAdmin());
    }

    /**
     * 게시글에 좋아요 표시
     * 중복으로 좋아요 표시해도 1개만 적용됩니다.
     *
     * @param id 게시글 id
     */
    @PostMapping("/like/{id}")
    @UserAuth
    public void like(AppAuthentication auth, @PathVariable Long id) {
        likeService.like(id, auth.getUserId(), POST);
    }

    /**
     * 좋아요 취소
     * 중복으로 좋아요 취소해도 최초 1건만 적용됩니다.
     *
     * @param id 게시글 id
     */
    @DeleteMapping("/like/{id}")
    @UserAuth
    public void cancelLike(AppAuthentication auth, @PathVariable Long id) {
        likeService.cancelLike(id, auth.getUserId(), LikeTarget.POST);
    }
}
