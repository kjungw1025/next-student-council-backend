package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectDownloadService {

    private final WebClient webClient;
    private final NHNAuthService nhnAuthService;

    public ResponseEntity<byte[]> downloadObject(String fileName, String fileUrl) {
        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", nhnAuthService.requestToken());
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));
        headers.setContentDispositionFormData("attachment", fileName);

        return webClient.get()
                .uri(fileUrl)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .exchangeToMono(value -> value.toEntity(new ParameterizedTypeReference<byte[]>() {
                }))
                .block();
    }
    
}
