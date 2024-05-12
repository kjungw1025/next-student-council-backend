package com.dku.council.domain.danfesta.controller;

import com.dku.council.domain.danfesta.model.FestivalDate;
import com.dku.council.domain.danfesta.model.dto.request.RequestCreateLineUpDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseFestivalDateDto;
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
     * <p>라인업 조회는 누구나 가능한 기능이며, FestivalDate는 축제 진행 일차(1일차)를 의미합니다.</p>
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
     * <p>라인업 등록은 관리자만 가능하며 festivalDate는 축제 진행 일차, performanceTime은 날짜를 의미합니다. </p>
     *
     * @param auth   사용자 정보
     * @param dto    라인업 등록 정보
     * @return       라인업 id
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @AdminAuth
    public ResponseIdDto create(AppAuthentication auth,
                                @Valid @ModelAttribute RequestCreateLineUpDto dto) {
        Long result = lineUpService.createLineUp(dto, auth.getUserId());
        return new ResponseIdDto(result);
    }

    /**
     * 일차별 라인업 공개 상태로 변경
     *
     * <p>공개 상태 변경은 boolean타입의 isOpened 값을 false 에서 true로 바꿔줍니다.</p>
     */
    @PatchMapping
    @AdminAuth
    public void changeToTrue(AppAuthentication auth, @RequestParam FestivalDate festivalDate) {
        lineUpService.changeToTrue(auth.getUserId(), festivalDate);
    }

    /**
     * 라인업 날짜 조회
     * <p>라인업 날짜는 중복되지 않게 조회됩니다.</p>
     */
    @GetMapping("/date")
    public List<ResponseFestivalDateDto> listFestivalDate() {
        return lineUpService.listFestivalDate();
    }
}
