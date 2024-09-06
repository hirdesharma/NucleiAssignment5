package com.example.user_service.service;

import com.example.user_service.model.User;

public interface UserServiceInterface {
  User registerUser(final User user);

  User authenticate(final String email, final String password);
}
