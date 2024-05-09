package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.exception.AlreadyInStorageException;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.model.ChatUploadedImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatImageUploadService {

    private final NHNAuthService nhnAuthService;
    private final ObjectUploadContext uploadContext;
    private final ObjectStorageService s3service;

    public Context newContext() {
        String token = nhnAuthService.requestToken();
        return new Context(token);
    }

    public class Context {
        private final String token;

        private Context(String token) { this.token = token; }

        public ArrayList<ChatUploadedImage> uploadChatImages(List<ImageRequest> images, String roomId, Long userId) {
            ArrayList<ChatUploadedImage> chatImages = new ArrayList<>();
            for (ImageRequest req : images) {
                chatImages.add(uploadChatImage(req, roomId, userId));
            }
            return chatImages;
        }

        public ChatUploadedImage uploadChatImage(ImageRequest image, String roomId, Long userId) {
            String originName = image.getOriginalFilename();
            if (originName == null) originName = "";

            String prefix = originName.substring(0, originName.lastIndexOf("."));
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            String imageId;
            do {
                imageId = uploadContext.makeImageId(prefix, ext);
            } while (s3service.isInChatImagePath(roomId, imageId));
            return upload(image, roomId, userId, imageId);
        }

        public ChatUploadedImage uploadChatImageWithName(ImageRequest image, String roomId, Long userId, String objectName) {
            if (s3service.isInChatImagePath(roomId, objectName)) {
                throw new AlreadyInStorageException();
            }
            return upload(image, roomId, userId, objectName);
        }

        private ChatUploadedImage upload(ImageRequest image, String roomId, Long userId, String objectName) {
            try {
                s3service.uploadChatImage(token, roomId, objectName, image.getInputStream(), image.getContentType());
                String chatImageUrl = uploadContext.getChatImageUrl(roomId, objectName);

                return new ChatUploadedImage(roomId, userId, chatImageUrl, image);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }

        public void deleteChatImage(String fileUrl) {
            s3service.deleteChatFileByDirectUrl(token, fileUrl);
        }
    }
}
