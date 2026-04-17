package com.emi.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.product.dtos.ProductRequest;
import com.emi.product.dtos.ProductResponse;
import com.emi.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
  
  private final ProductService productService;

  @PostMapping("/create")
  public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request){
    return ResponseEntity.ok(productService.createProduct(request));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id){
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @GetMapping("/all")
  public ResponseEntity<Iterable<ProductResponse>> listProducts(){
    return ResponseEntity.ok(productService.getAllProducts());
  }

}
