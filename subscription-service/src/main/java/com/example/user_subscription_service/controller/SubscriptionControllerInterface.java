package com.example.user_subscription_service.controller;

import com.example.user_subscription_service.model.Subscription;
import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

public interface SubscriptionControllerInterface {
  Subscription addSubscription(@RequestBody final Subscription subscription);

  List<Subscription> getAllSubscriptions();
}
