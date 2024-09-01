package com.example.user_service.service;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RedisService redisService;

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Override
  public final User registerUser(final User user) {
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());

    final String cacheKey = "user:" + user.getEmail();
    redisService.set(cacheKey, user, 3600L);

    User savedUser = userRepository.save(user);

    // Send Kafka message upon successful registration
    kafkaTemplate.send("registrationNotify", "User registered successfully: " + user.getEmail());

    return savedUser;
  }

  @Override
  public final User authenticate(final String email, final String password) {
    final String cacheKey = "user:" + email;
    Optional<User> cachedUser = redisService.get(cacheKey, User.class);
    if (cachedUser.isPresent()) {
      return cachedUser.get();
    }

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"));
    if (user.getPassword().equals(password)) {
      return user;
    } else {
      throw new RuntimeException("Invalid credentials");
    }
  }
}
