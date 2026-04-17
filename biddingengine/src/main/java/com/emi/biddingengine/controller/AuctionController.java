package com.emi.biddingengine.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.biddingengine.dtos.AuctionRequest;
import com.emi.biddingengine.dtos.AuctionResponse;
import com.emi.biddingengine.services.AuctionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {
  
  private final AuctionService auctionService;

  @PostMapping("/create")
  public ResponseEntity<String> createAuction(@RequestBody AuctionRequest request) {
    return ResponseEntity.ok(auctionService.createAuction(request));
  }

  @GetMapping("/all")
  public ResponseEntity<List<AuctionResponse>> getAllAuctions() {
    return ResponseEntity.ok(auctionService.getAllAuctions());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(auctionService.getAuctionById(id));  
  }

  @PostMapping("/close/{id}")
  public ResponseEntity<String> closeAuction(@PathVariable(value = "id") Long id) {
    return ResponseEntity.ok(auctionService.closeAuction(id));
  }

}
