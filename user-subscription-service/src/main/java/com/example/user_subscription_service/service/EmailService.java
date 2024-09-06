package com.example.user_subscription_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender emailSender;

  public void sendSubscriptionNotification(String to, String subscriptionDetails) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject("Subscription Confirmation");
    message.setText("Thank you for subscribing! " + subscriptionDetails);
    emailSender.send(message);
  }
}