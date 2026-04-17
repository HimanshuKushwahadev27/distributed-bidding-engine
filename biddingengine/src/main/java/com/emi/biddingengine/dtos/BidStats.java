package com.emi.biddingengine.dtos;

public record BidStats(
  Double maxBid,
  Double minBid,
  Double avgBid,
  Double currentHighestBid
) {
  
}
