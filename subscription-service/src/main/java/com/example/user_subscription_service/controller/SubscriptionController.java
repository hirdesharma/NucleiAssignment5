package com.example.user_subscription_service.controller;

import com.example.user_subscription_service.model.Subscription;
import com.example.user_subscription_service.service.SubscriptionServiceInterface;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController implements SubscriptionControllerInterface {

  @Autowired
  private SubscriptionServiceInterface subscriptionService;

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public final Subscription addSubscription(@Valid @RequestBody final Subscription subscription) {
    return subscriptionService.addSubscription(subscription);
  }

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public final List<Subscription> getAllSubscriptions() {
    return subscriptionService.getAllSubscriptions();
  }
}
