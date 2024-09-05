package com.example.notification;

import com.example.notification.service.EmailSenderService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaUserRegistrationListener {

  EmailSenderService emailSenderService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = "registrationNotify", groupId = "notification-group")
  void listener(String data) throws Exception {
    try {
      JsonNode jsonNode = objectMapper.readTree(data);
      String userId = jsonNode.get("userId").asText();
      String email = jsonNode.get("email").asText();

      System.out.println("Processed subscription for user: " + userId);
      String body = "Congratulations you have successfully registered on out newsletter with "
          + "email : " + email;
      emailSenderService.sendEmail(email, body);
    } catch (Exception e) {
      System.out.println(e);
      throw new Exception(e);
    }
  }
}
