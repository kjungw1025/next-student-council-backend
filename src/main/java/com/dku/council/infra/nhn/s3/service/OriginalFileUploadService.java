package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.exception.AlreadyInStorageException;
import com.dku.council.infra.nhn.global.exception.InvalidAccessObjectStorageException;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.OriginalUploadedImage;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class OriginalFileUploadService {

    //TODO 이 클래스는 기존에 게시물에 이미지를 첨부할 때 사용한 클래스(혼동X)

    private final NHNAuthService nhnAuthService;
    private final ObjectStorageService s3service;
    private final ObjectUploadContext uploadContext;


    public Context newContext() {
        String token = nhnAuthService.requestToken();
        return new Context(token);
    }

    public class Context {
        private final String token;

        private Context(String token) {
            this.token = token;
        }

        public ArrayList<OriginalUploadedImage> originalUploadFiles(List<ImageRequest> files, String prefix) {
            ArrayList<OriginalUploadedImage> postFiles = new ArrayList<>();
            for (ImageRequest req : files) {
                postFiles.add(originalUploadFile(req, prefix));
            }
            return postFiles;
        }

        public OriginalUploadedImage originalUploadFile(ImageRequest file, String prefix) {
            String originName = file.getOriginalFilename();
            if (originName == null) originName = "";

            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            String fileId;
            do {
                fileId = uploadContext.makeObjectId(prefix, ext);
            } while (s3service.isInObject(fileId));
            return originalUpload(file, fileId);
        }

        public OriginalUploadedImage originalUploadFileWithName(ImageRequest file, String objectName) {
            if (s3service.isInObject(objectName)) {
                throw new AlreadyInStorageException();
            }
            return originalUpload(file, objectName);
        }

        private OriginalUploadedImage originalUpload(ImageRequest file, String objectName) {
            try {
                s3service.uploadObject(token, objectName, file.getInputStream(), file.getContentType());
                return new OriginalUploadedImage(objectName, file);
            } catch (Throwable e) {
                throw new InvalidAccessObjectStorageException(e);
            }
        }

        public void originalDeleteFile(String fileId) {
            s3service.deleteObject(token, fileId);
        }
    }
}
