package com.example.user_subscription_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.user_subscription_service.model.UserSubscription;
import com.example.user_subscription_service.repository.UserSubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.List;

class UserSubscriptionServiceTest {

  @Mock
  private UserSubscriptionRepository userSubscriptionRepository;

  @Mock
  private RedisService redisService;

  @InjectMocks
  private UserSubscriptionService userSubscriptionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testSubscribe() {
    // Arrange
    UserSubscription userSubscription = new UserSubscription();
    userSubscription.setUserId(2L);
    userSubscription.setSubscriptionId(1L);

    when(userSubscriptionRepository.save(any(UserSubscription.class))).thenReturn(userSubscription);
    when(userSubscriptionRepository.findByUserId(anyLong())).thenReturn(List.of(userSubscription));

    // Act
    UserSubscription result = userSubscriptionService.subscribe(userSubscription);

    // Assert
    assertEquals(30L, result.getDuration());
    assertEquals(LocalDate.now(), result.getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.getEndDate());
    assertEquals(LocalDate.now(), result.getUpdatedAt());
    assertTrue(result.getIsActive());

    verify(userSubscriptionRepository, times(1)).save(userSubscription);
    verify(redisService, times(1)).set("userSubscriptions:" + userSubscription.getUserId(), List.of(userSubscription), 3600L);
  }

  @Test
  void testSaveUserSubscriptionToDB() {
    // Arrange
    UserSubscription userSubscription = new UserSubscription();
    userSubscription.setUserId(3L);
    userSubscription.setSubscriptionId(2L);

    when(userSubscriptionRepository.save(any(UserSubscription.class))).thenReturn(userSubscription);

    // Act
    UserSubscription result = userSubscriptionService.subscribe(userSubscription);

    // Assert
    assertEquals(30L, result.getDuration());
    assertEquals(LocalDate.now(), result.getStartDate());
    assertEquals(LocalDate.now().plusDays(30), result.getEndDate());
    assertEquals(LocalDate.now(), result.getUpdatedAt());
    assertTrue(result.getIsActive());

    verify(userSubscriptionRepository, times(1)).save(userSubscription);
  }

  @Test
  void testGetUserSubscriptions() {
    // Arrange
    Long userId = 4L;
    UserSubscription subscription1 = new UserSubscription();
    subscription1.setUserId(userId);
    subscription1.setSubscriptionId(101L);
    subscription1.setStartDate(LocalDate.now());
    subscription1.setEndDate(LocalDate.now().plusDays(20));
    subscription1.setIsActive(true);

    UserSubscription subscription2 = new UserSubscription();
    subscription2.setUserId(userId);
    subscription2.setSubscriptionId(102L);
    subscription2.setStartDate(LocalDate.now());
    subscription2.setEndDate(LocalDate.now().plusDays(25));
    subscription2.setIsActive(false);

    List<UserSubscription> subscriptions = List.of(subscription1, subscription2);
    when(userSubscriptionRepository.findByUserId(userId)).thenReturn(subscriptions);

    // Act
    List<UserSubscription> result = userSubscriptionService.getUserSubscriptions(userId);

    // Assert
    assertEquals(2, result.size());
    assertEquals(subscription1.getSubscriptionId(), result.get(0).getSubscriptionId());
    assertEquals(subscription1.getIsActive(), result.get(0).getIsActive());

    assertEquals(subscription2.getSubscriptionId(), result.get(1).getSubscriptionId());
    assertEquals(subscription2.getIsActive(), result.get(1).getIsActive());

    verify(userSubscriptionRepository, times(1)).findByUserId(userId);
  }
}
