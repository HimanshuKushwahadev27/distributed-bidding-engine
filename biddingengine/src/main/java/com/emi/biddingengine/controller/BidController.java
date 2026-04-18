package com.emi.biddingengine.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emi.biddingengine.dtos.BidRequest;
import com.emi.biddingengine.dtos.BidResults;
import com.emi.biddingengine.dtos.BidStats;
import com.emi.biddingengine.serviceImpl.Stimulate;
import com.emi.biddingengine.services.BidService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {
  
  private final Map<String, BidService> bidService;
  private final Stimulate stimulate;

  @PostMapping("/place/{type}")
  public ResponseEntity<BidResults> placeBid(
    @RequestBody BidRequest request,
    @PathVariable(value = "type") String type) {
    return ResponseEntity.ok(bidService.get(type).placeBid(request));
  }

@PostMapping("/simulate/{auctionId}/{users}/{type}")
public ResponseEntity<List<BidResults>> simulateBids(
 @PathVariable(value = "auctionId") Long auctionId,     
 @PathVariable(value = "users") int users,
 @PathVariable(value = "type") String type) {

      return ResponseEntity.ok(stimulate.stimulateBids(users, auctionId, type));
  }

  @GetMapping("/stats/{auctionId}/{type}")
  public ResponseEntity<BidStats> getAuctionStats(
    @PathVariable(value = "auctionId") Long auctionId,
    @PathVariable(value = "type") String type) {
    return ResponseEntity.ok(bidService.get(type).getAuctionStats(auctionId));
  }

}
