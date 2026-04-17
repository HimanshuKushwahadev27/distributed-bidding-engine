package com.emi.biddingengine.controller;

import java.util.List;

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
import com.emi.biddingengine.services.BidService;
import com.emi.biddingengine.services.Stimulate;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/bids")
@RequiredArgsConstructor
public class BidController {
  
  private final BidService bidService;
  private final Stimulate stimulate;

  @PostMapping("/place")
  public ResponseEntity<BidResults> placeBid(@RequestBody BidRequest request) {
    return ResponseEntity.ok(bidService.placeBid(request));
  }

  @PostMapping("/simulate/{auctionId}/{users}/{type}")
  public ResponseEntity<List<BidResults>> simulateBids(@PathVariable(value = "auctionId") Long auctionId,    @PathVariable(value = "users") int users,
    @PathVariable(value = "type") String type) {
    
    if(type.equalsIgnoreCase("naive")) {
      return ResponseEntity.ok(stimulate.stimulateNaiveBids(users, auctionId));
    }else{
      throw new IllegalArgumentException("Unsupported simulation type: " + type);
    }

  }

  @GetMapping("/stats/{auctionId}")
  public ResponseEntity<BidStats> getAuctionStats(@PathVariable(value = "auctionId") Long auctionId) {
    return ResponseEntity.ok(bidService.getAuctionStats(auctionId));
  }

}
