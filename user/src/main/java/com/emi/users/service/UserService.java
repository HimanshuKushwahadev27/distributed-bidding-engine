package com.emi.users.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.emi.users.dtos.ResponseUser;
import com.emi.users.dtos.UserRequest;
import com.emi.users.entity.UserInfo;
import com.emi.users.respository.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
  
  private final UserRepo userRepo;

  public String createUser(UserRequest userRequest){
    // Check if username or email already exists
    if (userRepo.existsByUsername(userRequest.username())) {
        return "Username already exists";
    }
    if (userRepo.existsByEmail(userRequest.email())) {
        return "Email already exists";
    }

    // Create new user
    UserInfo userInfo = new UserInfo();
    userInfo.setUsername(userRequest.username());
    userInfo.setEmail(userRequest.email());
    userInfo.setPassword(userRequest.password()); // In a real application, make sure to hash the password
    userInfo.setCreatedAt(Instant.now());

    userRepo.save(userInfo);
    return "User created successfully";
  }

  public ResponseUser getUserById(Long id) {
    UserInfo userInfo = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    return new ResponseUser(userInfo.getId(), userInfo.getUsername(), userInfo.getEmail(), userInfo.getCreatedAt());
  }
}
