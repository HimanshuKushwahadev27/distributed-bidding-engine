package com.emi.product.dtos;

public record ProductResponse(
    Long id,
    String name,
    String description,
    double price,
    java.time.Instant createdAt
) {

}
