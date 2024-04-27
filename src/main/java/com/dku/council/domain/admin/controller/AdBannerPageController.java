package com.dku.council.domain.admin.controller;

import com.dku.council.domain.admin.dto.AdBannerPageDto;
import com.dku.council.domain.admin.service.AdBannerPageService;
import com.dku.council.domain.banner.model.dto.request.RequestCreateBannerDto;
import com.dku.council.domain.banner.model.dto.response.ResponseBannerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/manage/ad-banner")
@RequiredArgsConstructor
public class AdBannerPageController {

    private final AdBannerPageService service;

    @GetMapping
    public String adBanner(Model model, RequestCreateBannerDto dto) {
        List<AdBannerPageDto> results = service.getAdBanner();
        model.addAttribute("banners", results);
        model.addAttribute("object", dto);
        return "page/ad-banner/ad-banner";
    }

    @PostMapping("/create")
    public String createAdBanner(HttpServletRequest request, RequestCreateBannerDto dto) {
        service.createBanner(dto);
        return "redirect:" + request.getHeader("Referer");
    }

    @PostMapping("/{adBannerId}/delete")
    public String deleteAdBanner(HttpServletRequest request, @PathVariable Long adBannerId) {
        service.deleteBanner(adBannerId);
        return "redirect:" + request.getHeader("Referer");
    }
}
