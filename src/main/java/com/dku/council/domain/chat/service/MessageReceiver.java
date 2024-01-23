package com.dku.council.domain.chat.service;

import com.dku.council.domain.chat.model.dto.response.ResponseChatDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageReceiver {

    private final SimpMessageSendingOperations template;

    @KafkaListener(topics = "chatting", containerFactory = "kafkaConsumerContainerFactory")
    public void receiveMessage(ResponseChatDto message) {
        log.info("전송할 채팅방 번호 = /sub/chatRoom/enter" + message.getRoomId(), message);
        log.info("채팅방으로 메시지 전송 = {}", message);

        // 메시지 객체 내부의 채팅방 번호를 참조하여, 해당 채팅방 구독자에게 메시지를 발송
        template.convertAndSend("/sub/chatRoom/enter" + message.getRoomId(), message);
    }
}