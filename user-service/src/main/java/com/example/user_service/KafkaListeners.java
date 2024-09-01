package com.example.user_service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
  @KafkaListener(topics = "registrationNotify", groupId = "notification-group")
  void listener(String data) {
    System.out.println("Listener received: " + data);
  }
}
