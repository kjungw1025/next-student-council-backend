package com.dku.council.domain.chat.service;

import com.dku.council.domain.chat.model.dto.response.ResponseChatDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender {
    private final KafkaTemplate<String, ResponseChatDto> kafkaTemplate;

    // 메시지를 지정한 Kafka 토픽으로 전송
    public void send(String topic, ResponseChatDto data) {

        // 메시지를 KafkaTemplate 를 사용하여 지정된 토픽으로 전송
        kafkaTemplate.send(topic, data);
    }
}