package com.org.shop_auth_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishMessage(String topic, Object message) {
        try {
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            log.error("[KafkaProducerService][publishMessage] поулчена ошибка: {}", e.getMessage(), e);
        }
    }
}
