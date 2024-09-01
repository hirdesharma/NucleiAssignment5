package com.example.user_subscription_service.controller;

import com.example.user_subscription_service.configure.KafkaProducerConfig;
import com.example.user_subscription_service.model.UserSubscription;
import com.example.user_subscription_service.service.UserSubscriptionServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
@RequestMapping("/api/user-subscriptions")
@Slf4j
public class UserSubscriptionController implements UserSubscriptionControllerInterface {

  @Autowired
  private UserSubscriptionServiceInterface userSubscriptionService;
  @Autowired
  KafkaProducerConfig producerConfig;

  @Override
  @PostMapping("/subscribe")
  @ResponseStatus(HttpStatus.OK)
  public final UserSubscription subscribe(
      @Valid @RequestBody final UserSubscription userSubscription) {
    try {
      return userSubscriptionService.subscribe(userSubscription);
    } catch (Exception e) {
      log.error("Error subscribing user: {}", e.getMessage(), e);
      throw e;
    }
  }

  @Override
  @GetMapping("/user/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public final List<UserSubscription> getUserSubscriptions(@Valid @PathVariable Long userId) {
    return userSubscriptionService.getUserSubscriptions(userId);
  }
}
