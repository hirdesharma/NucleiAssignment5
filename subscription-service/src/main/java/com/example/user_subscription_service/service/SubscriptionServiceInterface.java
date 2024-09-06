package com.example.user_subscription_service.service;

import com.example.user_subscription_service.model.Subscription;
import java.util.List;

public interface SubscriptionServiceInterface {
  Subscription addSubscription(final Subscription subscription);

  List<Subscription> getAllSubscriptions();
}
