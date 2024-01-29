package com.dku.council.domain.chatmessage.service;

import com.dku.council.domain.chatmessage.model.entity.ChatRoomMessage;
import com.dku.council.domain.chatmessage.repository.ChatRoomMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomMessageService {

    private final ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

    private final ChatRoomMessageRepository chatRoomMessageRepository;

    public void create(String roomId,
                       String messageType,
                       Long userId,
                       String userNickname,
                       String content) {

        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setRoomId(roomId);
        chatRoomMessage.setMessageType(messageType);
        chatRoomMessage.setUserId(userId);
        chatRoomMessage.setUserNickname(userNickname);
        chatRoomMessage.setContent(content);
        chatRoomMessage.setCreatedAt(LocalDateTime.now().atZone(seoulZoneId).toLocalDateTime());

        chatRoomMessageRepository.save(chatRoomMessage);
    }

    public List<ChatRoomMessage> findAllChatRoomMessages(String roomId) {
        return chatRoomMessageRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId);
    }

    public void deleteChatRoomMessages(String roomId) {
        chatRoomMessageRepository.deleteAllByRoomId(roomId);
    }
}
