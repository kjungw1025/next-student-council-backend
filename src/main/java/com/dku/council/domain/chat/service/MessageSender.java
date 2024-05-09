package com.dku.council.domain.chat.service;

import com.dku.council.domain.chat.model.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    // 메시지를 지정한 Kafka 토픽으로 전송
    public void send(String topic, Message data) {

        // 메시지를 KafkaTemplate 를 사용하여 지정된 토픽으로 전송
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String stringChat = objectMapper.writeValueAsString(data);
            log.info("MessageSender Message -> String형 : " + stringChat);
            kafkaTemplate.send(topic, stringChat);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}