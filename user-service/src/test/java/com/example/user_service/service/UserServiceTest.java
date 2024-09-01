package com.example.user_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.user_service.model.User;
import com.example.user_service.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testRegisterUserSuccess() {
    User user = new User();
    user.setEmail("hirdesh@example.com");
    user.setPassword("password123");

    User savedUser = new User();
    savedUser.setEmail(user.getEmail());
    savedUser.setPassword(user.getPassword());

    when(userRepository.save(any(User.class))).thenReturn(savedUser);

    User result = userService.registerUser(user);

    assertEquals(savedUser.getEmail(), result.getEmail());
    assertEquals(savedUser.getPassword(), result.getPassword());
    assertEquals(savedUser.getCreatedAt(), result.getCreatedAt());
    assertEquals(savedUser.getUpdatedAt(), result.getUpdatedAt());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void testRegisterUserFailure() {
    User user = new User();
    user.setEmail("hirdesh@gmail.com");
    user.setPassword("123456789");

    when(userRepository.save(any(User.class))).thenReturn(user);

    User newUser =  userService.registerUser(user);
    assertEquals(user,newUser);
  }

  @Test
  void testAuthenticateSuccess() {
    User user = new User();
    user.setId(1L);
    user.setEmail("test@example.com");
    user.setPassword("password123");

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    User result = userService.authenticate(user.getEmail(), user.getPassword());

    assertEquals(user.getId(), result.getId());
    assertEquals(user.getEmail(), result.getEmail());
    assertEquals(user.getPassword(), result.getPassword());
    verify(userRepository, times(1)).findByEmail(user.getEmail());
  }

  @Test
  void testAuthenticateUserNotFound() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> userService.authenticate("hirdesh@gmail.com", "password123"));

    verify(userRepository, times(1)).findByEmail("hirdesh@gmail.com");
  }

  @Test
  void testAuthenticateInvalidPassword() {
    User user = new User();
    user.setEmail("hirdesh@gmail.com");
    user.setPassword("password123");

    when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

    assertThrows(RuntimeException.class,
        () -> userService.authenticate(user.getEmail(), "wrongpassword"));

    verify(userRepository, times(1)).findByEmail(user.getEmail());
  }
}
