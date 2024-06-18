package com.example.ubersocketserver.consumers;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService1 {

    @KafkaListener(topics = "sample-topic",groupId = "sample-topic-3")
    public void listen(String message) {
        System.out.println("Kafka Consumer new message from topic sample topic new: " + message);
    }

}
