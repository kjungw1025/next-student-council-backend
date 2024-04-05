package com.dku.council.domain.danfesta.controller;

import com.dku.council.domain.danfesta.model.dto.response.ResponseStampInfoDto;
import com.dku.council.domain.danfesta.service.StampService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "스탬프", description = "스탬프 미션 관련 API")
@RequestMapping("/stamp")
public class StampController {

    private final StampService stampService;

    /**
     * 기본 스탬프 정보 생성
     * <p>부스별 미션을 수행하기 위한 사전단계로 제일 먼저 사용되어야 합니다.</p>
     */
    @PostMapping
    @UserAuth
    public void createDefaultSpecialMission(AppAuthentication auth) {
        stampService.createDefaultStampInfo(auth.getUserId());
    }

    /**
     * 현재 내가 받은 스탬프 조회
     */
    @PostMapping("/my")
    @UserAuth
    public ResponseStampInfoDto getMySpecialMission(AppAuthentication auth) {
        return stampService.getStampInfo(auth.getUserId());
    }
}
