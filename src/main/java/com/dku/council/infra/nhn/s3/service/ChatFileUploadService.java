package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.exception.InvalidAccessObjectStorageException;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import com.dku.council.infra.nhn.s3.model.ChatUploadedFile;
import com.dku.council.infra.nhn.s3.model.FileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatFileUploadService {

    private final NHNAuthService nhnAuthService;
    private final ObjectStorageService s3service;
    private final ObjectUploadContext uploadContext;

    public Context newContext() {
        String token = nhnAuthService.requestToken();
        return new Context(token);
    }

    public class Context {
        private final String token;

        private Context(String token) { this.token = token; }

        public ArrayList<ChatUploadedFile> uploadChatFiles(List<FileRequest> files, String roomId, Long userId) {
            ArrayList<ChatUploadedFile> chatFiles = new ArrayList<>();
            for (FileRequest req : files) {
                chatFiles.add(uploadChatFile(req, roomId, userId));
            }
            return chatFiles;
        }

        public ChatUploadedFile uploadChatFile(FileRequest file, String roomId, Long userId) {
            String originName = file.getOriginalFilename();
            if (originName == null) originName = "";

            String prefix = originName.substring(0, originName.lastIndexOf("."));
            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            String fileId;
            do {
                fileId = uploadContext.makeFileId(prefix, ext);
            } while (s3service.isInChatFilePath(roomId, fileId));
            return upload(file, roomId, userId, fileId);
        }

        private ChatUploadedFile upload(FileRequest file, String roomId, Long userId, String objectName) {
            try {
                s3service.uploadChatFile(token, roomId, objectName, file.getInputStream(), file.getContentType());
                String chatFileUrl = uploadContext.getChatFileUrl(roomId, objectName);

                return new ChatUploadedFile(roomId, userId, chatFileUrl, file);
            } catch (Throwable e) {
                throw new InvalidAccessObjectStorageException(e);
            }
        }

        public void deleteChatFile(String fileUrl) {
            s3service.deleteChatFileByDirectUrl(token, fileUrl);
        }
    }
}
