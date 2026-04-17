package com.emi.users.dtos;

public record UserRequest(
    String username,
    String email,
    String password
) {

}
