package com.dku.council.domain.danfesta.controller;

import com.dku.council.domain.danfesta.model.dto.response.ResponseSpecialMissionForAdminDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseSpecialMissionInfoDto;
import com.dku.council.domain.danfesta.service.SpecialGuestService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.AdminAuth;
import com.dku.council.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "특별 게스트 미션", description = "특별 미션 관련 API")
@RequestMapping("/special-guest")
public class SpecialGuestController {

    private final SpecialGuestService specialGuestService;

    /**
     * 기본 특별 게스트 미션 생성
     * <p>부스별 미션을 수행하기 위한 사전단계로 제일 먼저 사용되어야 합니다.</p>
     */
    @PostMapping
    @UserAuth
    public void createDefaultSpecialMission(AppAuthentication auth) {
        specialGuestService.createDefaultSpecialMission(auth.getUserId());
    }

    /**
     * 현재 내가 진행한 특별 미션 정보 조회
     */
    @PostMapping("/my")
    @UserAuth
    public ResponseSpecialMissionInfoDto getMySpecialMission(AppAuthentication auth) {
        return specialGuestService.getSpecialMissionInfo(auth.getUserId());
    }

    /**
     * 학번으로 사용자별 특별 미션 정보 조회 (Admin)
     */
    @GetMapping("/{studentId}")
    @AdminAuth
    public ResponseSpecialMissionForAdminDto getStudentSpecialMission(AppAuthentication auth,
                                                                      @PathVariable String studentId) {
        return specialGuestService.getSpecialMissionInfoForAdmin(auth.getUserId(), studentId);
    }

    /**
     * 특별 미션 스탬프 찍기 (Admin)
     */
    @PostMapping("/{studentId}")
    @AdminAuth
    public void stampSpecialMission(AppAuthentication auth,
                                    @PathVariable String studentId, int boothNumber) {
        specialGuestService.stampSpecialMission(auth.getUserId(), studentId, boothNumber);
    }
}
