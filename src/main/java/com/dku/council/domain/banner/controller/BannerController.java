package com.dku.council.domain.banner.controller;

import com.dku.council.domain.banner.model.dto.response.ResponseBannerDto;
import com.dku.council.domain.banner.model.dto.response.ResponseBannerUrlDto;
import com.dku.council.domain.banner.service.BannerService;
import com.dku.council.global.util.RemoteAddressUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "배너", description = "배너 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    /**
     * 전체 배너 조회
     */
    @GetMapping
    public List<ResponseBannerDto> getBannerList(HttpServletRequest request) {
        return bannerService.getBannerList(RemoteAddressUtil.getProxyableAddr(request));
    }

    /**
     * 배너 URL 조회
     * <p>배너 Id로 배너 URL을 조회합니다. 경로 Redirection는 새 창으로 해주세요.</p>
     *
     * @param id   배너 ID
     * @return     배너 URL
     */
    @GetMapping("/{id}")
    public ResponseBannerUrlDto getBannerUrl(@PathVariable Long id, HttpServletRequest request) {
        String result = bannerService.getBannerUrl(id, RemoteAddressUtil.getProxyableAddr(request));
        return new ResponseBannerUrlDto(result);
    }
}
