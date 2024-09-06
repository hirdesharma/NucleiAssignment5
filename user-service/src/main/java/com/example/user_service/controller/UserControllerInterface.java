package com.example.user_service.controller;

import com.example.user_service.model.User;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerInterface {
  User register(@RequestBody final User user);

  User login(@RequestBody final User user);
}
