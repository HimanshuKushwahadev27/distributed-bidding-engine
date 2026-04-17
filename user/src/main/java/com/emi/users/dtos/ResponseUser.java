package com.emi.users.dtos;

public record ResponseUser(
    Long id,
    String username,
    String email,
    java.time.Instant createdAt
) {

}
