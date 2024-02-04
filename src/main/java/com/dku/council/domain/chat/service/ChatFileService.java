package com.dku.council.domain.chat.service;

import com.dku.council.domain.chatmessage.model.entity.ChatRoomMessage;
import com.dku.council.domain.chatmessage.repository.ChatRoomMessageRepository;
import com.dku.council.infra.nhn.global.service.service.NHNAuthService;
import com.dku.council.infra.nhn.s3.service.ObjectStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatFileService {

    private final NHNAuthService nhnAuthService;
    private final ObjectStorageService objectStorageService;

    private final ChatRoomMessageRepository chatRoomMessageRepository;

    public void deleteAllFilesInChatRoom(String roomId) {
        List<ChatRoomMessage> imageMessageList = chatRoomMessageRepository.findAllByRoomIdAndFileType(roomId, "IMAGE");
        List<ChatRoomMessage> fileMessageList = chatRoomMessageRepository.findAllByRoomIdAndFileType(roomId, "FILE");

        if (!imageMessageList.isEmpty()) {
            for (ChatRoomMessage chatRoomMessage : imageMessageList) {
                objectStorageService.deleteChatFileByDirectUrl(nhnAuthService.requestToken(), chatRoomMessage.getFileUrl());
            }
        }

        if (!fileMessageList.isEmpty()) {
            for (ChatRoomMessage chatRoomMessage : fileMessageList) {
                objectStorageService.deleteChatFileByDirectUrl(nhnAuthService.requestToken(), chatRoomMessage.getFileUrl());
            }
        }
    }
}
