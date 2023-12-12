package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.dku.exception.DkuFailedCrawlingException;
import com.dku.council.infra.nhn.global.exception.AlreadyInStorageException;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.UploadedImage;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageUploadService {

    private final NHNAuthService nhnAuthService;
    private final ObjectUploadContext uploadContext;
    private final ObjectStorageService s3service;

    public Context newContext() {
        String token = nhnAuthService.requestToken();
        return new Context(token);
    }

    public class Context {
        private final String token;

        private Context(String token) {
            this.token = token;
        }

        public ArrayList<UploadedImage> uploadImages(List<ImageRequest> images, String prefix) {
            ArrayList<UploadedImage> postImages = new ArrayList<>();
            for (ImageRequest req : images) {
                postImages.add(uploadImage(req, prefix));
            }
            return postImages;
        }

        public UploadedImage uploadImage(ImageRequest image, String prefix) {
            String originName = image.getOriginalFilename();
            if (originName == null) originName = "";

            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            String imageId;
            do {
                imageId = uploadContext.makeImageId(prefix, ext);
            } while (s3service.isInImagePath(imageId));
            return upload(image, imageId);
        }

        public UploadedImage uploadImageWithName(ImageRequest image, String objectName) {
            if (s3service.isInImagePath(objectName)) {
                throw new AlreadyInStorageException();
            }
            return upload(image, objectName);
        }

        private UploadedImage upload(ImageRequest image, String objectName) {
            try {
                s3service.uploadImage(token, objectName, image.getInputStream(), image.getContentType());
                return new UploadedImage(objectName, image);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public void deleteImage(String imageId) {
            s3service.deleteImage(token, imageId);
        }
    }
}
