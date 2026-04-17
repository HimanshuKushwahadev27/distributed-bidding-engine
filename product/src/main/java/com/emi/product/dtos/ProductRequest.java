package com.emi.product.dtos;

public record ProductRequest(
    String name,
    String description,
    double price
) {

}
