package com.emi.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.emi.product.dtos.ProductRequest;
import com.emi.product.dtos.ProductResponse;
import com.emi.product.entity.Product;
import com.emi.product.repository.ProductRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  
public class ProductService{

  private final ProductRepo productRepo;

  public ProductResponse createProduct(ProductRequest request){
    var product = new Product();
    product.setName(request.name());
    product.setDescription(request.description());
    product.setPrice(request.price());
    product.setCreatedAt(java.time.Instant.now());
    var savedProduct = productRepo.save(product);
    return new ProductResponse(savedProduct.getId(), savedProduct.getName(), savedProduct.getDescription(), savedProduct.getPrice(), savedProduct.getCreatedAt());
  }

  public ProductResponse getProductById(Long id){
    var product = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCreatedAt());
  }

  public List<ProductResponse> getAllProducts(){
    var products = productRepo.findAll();
    return products.stream()
            .map(product -> new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCreatedAt()))
            .toList();
  }
  
}