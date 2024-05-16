package com.dku.council.domain.health;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Tag(name = "Health Check", description = "서버 health 관련 api")
@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class HealthController {

    /**
     * health를 체크
     */
    @GetMapping
    public void healthCheck(@RequestHeader MultiValueMap<String, String> headers) {
        headers.forEach((key, value) -> log.info("Header: {}={}", key, value));
    }
}
