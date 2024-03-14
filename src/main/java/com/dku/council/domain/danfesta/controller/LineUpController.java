package com.dku.council.domain.danfesta.controller;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.dto.request.RequestCreateLineUpDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseLineUpDto;
import com.dku.council.domain.danfesta.service.LineUpService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.AdminAuth;
import com.dku.council.global.model.dto.ResponseIdDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "축제 라인업" , description = "축제 라인업 관련 API")
@RequestMapping("/line-up")
public class LineUpController {

    private final LineUpService lineUpService;

    /**
     * 라인업 목록 조회
     *
     * @param festivalDate   축제 일차
     */
    @GetMapping
    public List<ResponseLineUpDto> list(@RequestParam FestivalDate festivalDate) {
        return lineUpService.list(festivalDate);
    }

    /**
     * 라인업 등록
     *
     * @param auth   사용자 정보
     * @param dto    라인업 등록 정보
     * @return       라인업 id
     * <p>performanceDate의 시간은 상관 없이 넣으셔도 됩니다.</p>
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @ModelAttribute RequestCreateLineUpDto dto) {
        Long result = lineUpService.createLineUp(dto, auth.getUserId());
        return new ResponseIdDto(result);
    }

    /**
     * 라인업 공개 상태로 변경
     */
    @PatchMapping
    @AdminAuth
    public void changeToTrue(AppAuthentication auth) {
        lineUpService.changeToTrue(auth.getUserId());
    }
}
