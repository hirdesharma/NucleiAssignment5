package com.example.notification;

import com.example.notification.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class NotificationServiceApplication {

  @Autowired
  private EmailSenderService emailServiceSender;

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApplication.class, args);
  }

  @EventListener(ApplicationReadyEvent.class)
  public void sendMail() {
    emailServiceSender.sendEmail("sharmahirdesh001@gmail.com", "hello", "its me");
  }

  @Bean
  CommandLineRunner commandLineRunner(KafkaTemplate<String, String> kafkaTemplate) {
    return args -> {
      kafkaTemplate.send("hallo", "lambardaar sarkaar 🫡 hi");
    };
  }
}
