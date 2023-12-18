package com.dku.council.domain.with_dankook.controller;

import com.dku.council.domain.post.model.dto.response.ResponsePage;
import com.dku.council.domain.with_dankook.model.dto.list.SummarizedTradeDto;
import com.dku.council.domain.with_dankook.model.dto.request.RequestCreateTradeDto;
import com.dku.council.domain.with_dankook.service.TradeService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "단국 거래 게시판", description = "단국 거래 게시판 API")
@RestController
@RequestMapping("with-dankook/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    /**
     * 단국 거래 게시글 목록 조회
     *
     * @param keyword  제목이나 내용에 포함된 검색어. 지정하지 않으면 모든 게시글 조회.
     * @param bodySize 게시글 본문 길이. (글자 단위) 지정하지 않으면 50 글자.
     * @param pageable 페이징 size, sort, page
     * @return 페이징된 단국 거래 게시판 목록
     */
    @GetMapping
    public ResponsePage<SummarizedTradeDto> list(@RequestParam(required = false) String keyword,
                                                 @RequestParam(defaultValue = "50") int bodySize,
                                                 @ParameterObject Pageable pageable) {
        Page<SummarizedTradeDto> list = tradeService.list(keyword, pageable, bodySize);
        return new ResponsePage<>(list);
    }

    /**
     * 단국 거래 게시글 등록
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @ModelAttribute RequestCreateTradeDto dto) {
        Long id = tradeService.create(auth.getUserId(), dto);
        return new ResponseIdDto(id);
    }

    /**
     * 단국 거래 게시글 삭제
     *
     * @param auth 사용자 인증정보
     * @param id   삭제할 게시글 id
     */
    @DeleteMapping("/{id}")
    @UserAuth
    public void delete(AppAuthentication auth, @PathVariable Long id) {
        tradeService.delete(id, auth.getUserId(), auth.isAdmin());
    }
}
