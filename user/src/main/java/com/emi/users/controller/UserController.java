package com.emi.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.users.dtos.ResponseUser;
import com.emi.users.dtos.UserRequest;
import com.emi.users.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  
  private final UserService userService;  
  
  @PostMapping("/create")
  public ResponseEntity<String> createUser(UserRequest userRequest) {
    String result = userService.createUser(userRequest);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseUser> getUserById(@PathVariable Long id) {
    ResponseUser responseUser = userService.getUserById(id);
    return ResponseEntity.ok(responseUser);  
  }

}
