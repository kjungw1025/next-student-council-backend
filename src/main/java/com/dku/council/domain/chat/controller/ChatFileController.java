package com.dku.council.domain.chat.controller;

import com.dku.council.domain.chat.exception.InvalidChatRoomUserException;
import com.dku.council.domain.chat.model.dto.request.RequestChatFileDto;
import com.dku.council.domain.chat.service.ChatService;
import com.dku.council.global.auth.jwt.AppAuthentication;
import com.dku.council.global.auth.role.UserAuth;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import com.dku.council.infra.nhn.s3.model.ChatUploadedFile;
import com.dku.council.infra.nhn.s3.model.ChatUploadedImage;
import com.dku.council.infra.nhn.s3.model.FileRequest;
import com.dku.council.infra.nhn.s3.model.ImageRequest;
import com.dku.council.infra.nhn.s3.service.ChatFileUploadService;
import com.dku.council.infra.nhn.s3.service.ChatImageUploadService;
import com.dku.council.infra.nhn.s3.service.ObjectDownloadService;
import com.dku.council.infra.nhn.s3.service.ObjectStorageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "채팅방 파일", description = "채팅방 파일/이미지 업로드 및 다운로드 관련 api")
@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatFileController {

    private final ChatService chatService;
    private final ChatImageUploadService chatImageUploadService;
    private final ChatFileUploadService chatFileUploadService;
    private final ObjectDownloadService objectDownloadService;

    /**
     * 파일 업로드 기능
     *
     * @param request   roomId와 전송할 파일에 대한 dto
     */
    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public List<ChatUploadedFile> uploadFile(AppAuthentication auth,
                                             @Valid @ModelAttribute RequestChatFileDto request) {
        // 채팅방에 현재 참여중인 유저가 아니면, 해당 채팅방에 이미지 업로드를 할 수 없게
        if (chatService.alreadyInRoom(request.getRoomId(), auth.getUserId())) {
            return chatFileUploadService.newContext().uploadChatFiles(
                    FileRequest.ofList(request.getFiles()),
                    request.getRoomId(),
                    auth.getUserId());
        } else {
            throw new InvalidChatRoomUserException();
        }
    }

    /**
     * 이미지 업로드 기능
     *
     * @param request   roomId와 전송할 이미지 파일에 대한 dto
     */
    @PostMapping(value = "/image/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public List<ChatUploadedImage> uploadImage(AppAuthentication auth,
                                                @Valid @ModelAttribute RequestChatFileDto request) {
        // 채팅방에 현재 참여중인 유저가 아니면, 해당 채팅방에 이미지 업로드를 할 수 없게
        if (chatService.alreadyInRoom(request.getRoomId(), auth.getUserId())) {
            return chatImageUploadService.newContext().uploadChatImages(
                    ImageRequest.ofList(request.getFiles()),
                    request.getRoomId(),
                    auth.getUserId());
        } else {
            throw new InvalidChatRoomUserException();
        }
    }

    /**
     * 채팅방에 올린 파일 다운로드 기능
     *
     * @param fileName      다운로드 하고자하는 파일의 원본 이름
     * @param roomId        채팅방 id
     * @param fileUrl       파일 url
     */
    @GetMapping("/download/{fileName}")
    @UserAuth
    public ResponseEntity<byte[]> download(AppAuthentication auth,
                                           @PathVariable String fileName,
                                           @RequestParam("roomId") String roomId,
                                           @RequestParam("fileUrl") String fileUrl) {
        if (chatService.alreadyInRoom(roomId, auth.getUserId())) {
            return objectDownloadService.downloadObject(fileName, fileUrl);
        } else {
            throw new InvalidChatRoomUserException();
        }
    }

    // TODO : 파일 삭제시, chatRoomMessage에도 삭제된게 반영 되어야함
//    @DeleteMapping("/file/delete")
//    public void deleteFile(@RequestParam("roomId") String roomId,
//                            @RequestParam("fileUrl") String fileUrl) {
//        objectStorageService.deleteChatFileByDirectUrl(nhnAuthService.requestToken(), fileUrl);
//    }

}
