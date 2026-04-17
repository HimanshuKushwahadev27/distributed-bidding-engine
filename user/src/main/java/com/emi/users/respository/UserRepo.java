package com.emi.users.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.users.entity.UserInfo;

public interface UserRepo extends JpaRepository<UserInfo, Long> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);
  
}
