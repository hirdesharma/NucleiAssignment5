package com.example.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
  @Autowired
  private JavaMailSender javaMailSender;

  public void sendEmail(String toEmail, String userId, String subscriptionId) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("supernova108123@gmail.com");
    message.setTo(toEmail);
    message.setSubject("Subscription done");
    String body = "Congratulations you { userId : " + userId + "} have been subscribed to the "
        + "subscription {subscriptionId : " + "}";
    message.setText(body);
    javaMailSender.send(message);
    System.out.println("chala gaya mail");
  }
}
