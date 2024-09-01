package com.example.user_subscription_service.service;

import com.example.user_subscription_service.model.UserSubscription;
import com.example.user_subscription_service.repository.UserSubscriptionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserSubscriptionService implements UserSubscriptionServiceInterface {

  @Autowired
  private RedisService redisService;

  @Autowired
  private UserSubscriptionRepository userSubscriptionRepository;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public final UserSubscription subscribe(final UserSubscription userSubscription) {
    final UserSubscription savedSubscription = saveUserSubscriptionToDB(userSubscription);

    final String cacheKey = "userSubscriptions:" + userSubscription.getUserId();
    final List<UserSubscription> subscriptions = getUserSubscriptions(userSubscription.getUserId());

    redisService.set(cacheKey, subscriptions, 3600L);

    return savedSubscription;
  }

  private UserSubscription saveUserSubscriptionToDB(final UserSubscription userSubscription) {

    final long duration = 30L;
    final LocalDate now = LocalDate.now();

    userSubscription.setDuration(duration);
    userSubscription.setStartDate(now);
    userSubscription.setEndDate(LocalDate.now().plusDays(duration));
    userSubscription.setUpdatedAt(now);
    userSubscription.setIsActive(true);

    kafkaTemplate.send("subscription_events",
        "User " + userSubscription.getUserId() + " subscribed to " +
            userSubscription.getSubscriptionId());
    return userSubscriptionRepository.save(userSubscription);
  }

  @Override
  public final List<UserSubscription> getUserSubscriptions(final Long userId) {
    //    String cacheKey = "userSubscriptions:" + userId;
    //    Optional<List<UserSubscription>> cachedSubscriptions = redisService.get(cacheKey, List
    //    .class);
    //    if (cachedSubscriptions.isPresent()) {
    //      return cachedSubscriptions.get();
    //    }
    return userSubscriptionRepository.findByUserId(userId);
  }
}