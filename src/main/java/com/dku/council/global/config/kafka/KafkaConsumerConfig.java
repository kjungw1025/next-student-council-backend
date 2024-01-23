package com.dku.council.global.config.kafka;

import com.dku.council.domain.chat.model.dto.request.RequestChatDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String offset;

    // KafkaListener 컨테이너 펙토리를 생성하는 Bean 메서드
    @Bean
    ConcurrentKafkaListenerContainerFactory<String, RequestChatDto> kafkaConsumerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RequestChatDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // topic에 붙은 consumer의 offset정보가 존재하지 않는다면,
    // auto.offset.reset의 default값(latest)이나 또는 설정한 값을 따라가게 됨
    // latest : 가장 마지막 offset부터
    // earliest : 가장 처음 offset부터
    // none : 해당 consumer group이 가져가고자 하는 topic의 consumer offset정보가 없으면 exception을 발생시킴.
    @Bean
    public ConsumerFactory<String, RequestChatDto> consumerFactory() {
        JsonDeserializer<RequestChatDto> deserializer = new JsonDeserializer<>();
        // 패키지 신뢰 오류로 인해 모든 패키지를 신뢰하도록 설정
        deserializer.addTrustedPackages("*");

        Map<String, Object> consumerConfig = new HashMap<>();
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, topic);
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offset);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

        return new DefaultKafkaConsumerFactory<>(consumerConfig, new StringDeserializer(), deserializer);
    }
}