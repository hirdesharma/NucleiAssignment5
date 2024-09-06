package com.example.user_subscription_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.user_subscription_service.model.Subscription;
import com.example.user_subscription_service.repository.SubscriptionRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class SubscriptionServiceTest {

  @Mock
  private SubscriptionRepository subscriptionRepository;

  @InjectMocks
  private SubscriptionService subscriptionService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testAddSubscription() {
    Subscription subscription = new Subscription();
    subscription.setId(1L);
    subscription.setName("Premium");

    when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);

    Subscription savedSubscription = subscriptionService.addSubscription(subscription);

    assertNotNull(savedSubscription);
    assertEquals(1L, savedSubscription.getId());
    assertEquals("Premium", savedSubscription.getName());

    verify(subscriptionRepository, times(1)).save(any(Subscription.class));
  }

  @Test
  void testGetAllSubscriptions() {
    Subscription subscription1 = new Subscription();
    subscription1.setId(1L);
    subscription1.setName("Basic");

    Subscription subscription2 = new Subscription();
    subscription2.setId(2L);
    subscription2.setName("Premium");

    when(subscriptionRepository.findAll()).thenReturn(Arrays.asList(subscription1, subscription2));

    List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();

    assertNotNull(subscriptions);
    assertEquals(2, subscriptions.size());
    assertEquals("Basic", subscriptions.get(0).getName());
    assertEquals("Premium", subscriptions.get(1).getName());

    verify(subscriptionRepository, times(1)).findAll();
  }

  @Test
  void testAddSubscriptionWithNullSubscription() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      subscriptionService.addSubscription(null);
    });

    assertEquals("Subscription cannot be null", exception.getMessage());
    verify(subscriptionRepository, never()).save(any(Subscription.class));
  }

  @Test
  void testGetAllSubscriptionsWhenNoSubscriptions() {
    when(subscriptionRepository.findAll()).thenReturn(List.of());

    List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();

    assertNotNull(subscriptions);
    assertEquals(0, subscriptions.size());

    verify(subscriptionRepository, times(1)).findAll();
  }
}
