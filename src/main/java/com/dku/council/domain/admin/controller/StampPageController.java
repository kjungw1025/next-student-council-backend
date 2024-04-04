package com.dku.council.domain.admin.controller;

import com.dku.council.domain.admin.dto.StampPageDto;
import com.dku.council.domain.admin.service.StampPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import static com.dku.council.domain.admin.util.PageConstants.DEFAULT_MAX_PAGE;
import static com.dku.council.domain.admin.util.PageConstants.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping("/manage/stamps")
@RequiredArgsConstructor
public class StampPageController {

    private final StampPageService service;

    @GetMapping
    public String stamps(Model model, @PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable, @Nullable String studentId) {
        Page<StampPageDto> all = service.list(studentId, pageable);
        model.addAttribute("stamps", all);
        model.addAttribute("maxPage", DEFAULT_MAX_PAGE);
        model.addAttribute("studentId", studentId);
        return "page/stamp/stamps";
    }

    @PostMapping("/{studentId}/stampToMission")
    public String stampToMission(HttpServletRequest request, @PathVariable String studentId, int boothNumber) {
        service.stamp(studentId, boothNumber);
        return "redirect:" + request.getHeader("Referer");
    }
}
