package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.exception.InvalidAccessObjectStorageException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ObjectStorageService {

    private final WebClient webClient;
    private final ObjectUploadContext uploadContext;

    //TODO 기존 코드 삭제 예정
    public boolean isInObject(String objectName) {
        try {
            webClient.get()
                    .uri(uploadContext.getObjectUrl(objectName))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
        }
        return true;
    }

    /**
     * ~/imagetest/objectName 경로에 이미지가 있는지 확인합니다.
     */
    public boolean isInImagePath(String objectName) {
        try {
            webClient.get()
                    .uri(uploadContext.getImageUrl(objectName))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
        }
        return true;
    }

    /**
     * ~/filetest/objectName 경로에 파일이 있는지 확인합니다.
     */
    public boolean isInFilePath(String objectName) {
        try {
            webClient.get()
                    .uri(uploadContext.getFileUrl(objectName))
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                return false;
            }
        }
        return true;
    }

    //TODO 기존 코드 삭제 예정
    public void uploadObject(String tokenId, String objectName, final InputStream inputStream, @Nullable MediaType contentType) {
        try {
            WebClient.RequestBodySpec spec = webClient.put()
                    .uri(uploadContext.getObjectUrl(objectName))
                    .header("X-Auth-Token", tokenId);

            if (contentType != null) {
                spec = spec.header("Content-Type", contentType.toString());
            }

            spec.body(BodyInserters.fromResource(new InputStreamResource(inputStream)))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Throwable e) {
            throw new InvalidAccessObjectStorageException(e);
        }
    }

    /**
     * ~/imagetest/objectName 경로에 파일을 업로드합니다.
     */
    public void uploadImage(String tokenId, String objectName, final InputStream inputStream, @Nullable MediaType contentType) {
        try {
            WebClient.RequestBodySpec spec = webClient.put()
                    .uri(uploadContext.getImageUrl(objectName))
                    .header("X-Auth-Token", tokenId);

            if (contentType != null) {
                spec = spec.header("Content-Type", contentType.toString());
            }

            spec.body(BodyInserters.fromResource(new InputStreamResource(inputStream)))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Throwable e) {
            throw new InvalidAccessObjectStorageException(e);
        }
    }

    /**
     * ~/filetest/objectName 경로에 파일을 업로드합니다.
     */
    public void uploadFile(String tokenId, String objectName, final InputStream inputStream, @Nullable MediaType contentType) {
        try {
            WebClient.RequestBodySpec spec = webClient.put()
                    .uri(uploadContext.getFileUrl(objectName))
                    .header("X-Auth-Token", tokenId);

            if (contentType != null) {
                spec = spec.header("Content-Type", contentType.toString());
            }

            spec.body(BodyInserters.fromResource(new InputStreamResource(inputStream)))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Throwable e) {
            throw new InvalidAccessObjectStorageException(e);
        }
    }

    //TODO 기존 코드 삭제 예정
    public void deleteObject(String tokenId, String objectName) {
        try {
            webClient.delete()
                    .uri(uploadContext.getObjectUrl(objectName))
                    .header("X-Auth-Token", tokenId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Throwable e) {
            throw new InvalidAccessObjectStorageException(e);
        }
    }

    /**
     * ~/imagetest/objectName 경로에 있는 파일을 삭제합니다.
     */
    public void deleteImage(String tokenId, String objectName) {
        try {
            webClient.delete()
                    .uri(uploadContext.getImageUrl(objectName))
                    .header("X-Auth-Token", tokenId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Throwable e) {
            throw new InvalidAccessObjectStorageException(e);
        }
    }

    /**
     * ~/filetest/objectName 경로에 있는 파일을 삭제합니다.
     */
    public void deleteFile(String tokenId, String objectName) {
        try {
            webClient.delete()
                    .uri(uploadContext.getFileUrl(objectName))
                    .header("X-Auth-Token", tokenId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Throwable e) {
            throw new InvalidAccessObjectStorageException(e);
        }
    }

}
