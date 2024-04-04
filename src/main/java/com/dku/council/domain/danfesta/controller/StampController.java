package com.dku.council.domain.danfesta.controller;

import com.dku.council.domain.danfesta.model.dto.request.RequestGetStampInfoDto;
import com.dku.council.domain.danfesta.model.dto.request.RequestStampDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseStampForAdminDto;
import com.dku.council.domain.danfesta.model.dto.response.ResponseStampInfoDto;
import com.dku.council.domain.danfesta.service.StampService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.AdminAuth;
import com.dku.council.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

//    /**
//     * 학번으로 사용자별 특별 미션 정보 조회 (Admin)
//     */
//    @GetMapping("/{studentId}")
//    @AdminAuth
//    public ResponseStampForAdminDto getStudentSpecialMission(AppAuthentication auth,
//                                                             @Valid @RequestBody RequestGetStampInfoDto dto) {
//        return stampService.getStampInfoForAdmin(auth.getUserId(), dto.getStudentId());
//    }
//
//    /**
//     * 특별 미션 스탬프 찍기 (Admin)
//     */
//    @PostMapping("/{studentId}")
//    @AdminAuth
//    public void stampSpecialMission(AppAuthentication auth,
//                                    @Valid @RequestBody RequestStampDto dto) {
//        stampService.stamp(auth.getUserId(), dto.getStudentId(), dto.getBoothNumber());
//    }
}
