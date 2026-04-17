package com.emi.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.product.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Long> {
  
}
