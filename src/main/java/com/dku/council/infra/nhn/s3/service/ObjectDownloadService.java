package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ObjectDownloadService {

    private final NHNAuthService nhnAuthService;

    public ResponseEntity<byte[]> downloadObject(String fileName, String fileUrl) {

        // RestTemplate 생성
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Auth-Token", nhnAuthService.requestToken());
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));
        headers.setContentDispositionFormData("attachment", fileName);

        HttpEntity<String> requestHttpEntity = new HttpEntity<String>(null, headers);
        
        // API 호출, 데이터를 바이트 배열로 받음
        ResponseEntity<byte[]> response = restTemplate.exchange(fileUrl, HttpMethod.GET, requestHttpEntity, byte[].class);

        return response;
    }
    
}
