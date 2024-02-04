package com.dku.council.infra.nhn.s3.service;

import com.dku.council.infra.nhn.global.exception.InvalidAccessObjectStorageException;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import com.dku.council.infra.nhn.s3.model.FileRequest;
import com.dku.council.infra.nhn.s3.model.UploadedFile;
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

        public ArrayList<UploadedFile> uploadChatFiles(List<FileRequest> files, String roomId, String prefix) {
            ArrayList<UploadedFile> chatFiles = new ArrayList<>();
            for (FileRequest req : files) {
                chatFiles.add(uploadChatFile(req, roomId, prefix));
            }
            return chatFiles;
        }

        public UploadedFile uploadChatFile(FileRequest file, String roomId, String prefix) {
            String originName = file.getOriginalFilename();
            if (originName == null) originName = "";

            String ext = originName.substring(originName.lastIndexOf(".") + 1);
            String fileId;
            do {
                fileId = uploadContext.makeFileId(prefix, ext);
            } while (s3service.isInChatFilePath(roomId, fileId));
            return upload(file, roomId, fileId);
        }

        private UploadedFile upload(FileRequest file, String roomId, String objectName) {
            try {
                s3service.uploadChatFile(token, roomId, objectName, file.getInputStream(), file.getContentType());
                return new UploadedFile(objectName, file);
            } catch (Throwable e) {
                throw new InvalidAccessObjectStorageException(e);
            }
        }

        public void deleteChatFile(String roomId, String fileUrl) {
            s3service.deleteChatFileByDirectUrl(token, fileUrl);
        }
    }
}
