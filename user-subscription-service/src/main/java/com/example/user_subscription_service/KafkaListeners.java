package com.example.user_subscription_service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaListeners {
  @KafkaListener(topics = "subscription_events", groupId = "subscription-notification-group")
  void listener(String data) {
    System.out.println("Listener received " + data + " thanks hi ");
  }
}
