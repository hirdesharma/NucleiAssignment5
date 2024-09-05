package com.example.notification;

import com.example.notification.service.EmailSenderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaSubscriptionListeners {

  EmailSenderService emailSenderService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = "subscription_events", groupId = "subscription-notification-group")
  void listener(String data) throws Exception {
    try {
      JsonNode jsonNode = objectMapper.readTree(data);
      String userId = jsonNode.get("userId").asText();
      String subscriptionId = jsonNode.get("subscriptionId").asText();
      String email = jsonNode.get("email").asText();

      System.out.println("Processed subscription for user: " + userId);
      String body = "Congratulations you { userId : " + userId + "} have been subscribed to the "
          + "subscription {subscriptionId : " + "}";
      emailSenderService.sendEmail(email, body);
    } catch (Exception e) {
      System.out.println(e);
      throw new Exception(e);
    }
  }
}
