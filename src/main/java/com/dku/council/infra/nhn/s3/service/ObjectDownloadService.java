package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
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
        // 파일 이름을 UTF-8로 인코딩하여 Content-Disposition 헤더에 추가
        String encodedFileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        headers.setContentDispositionFormData("attachment", encodedFileName);

        return webClient.get()
                .uri(fileUrl)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .exchangeToMono(response -> response.toEntity(byte[].class))
                .block();
    }
    
}
