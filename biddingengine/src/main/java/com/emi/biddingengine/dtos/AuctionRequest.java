package com.emi.biddingengine.dtos;

public record AuctionRequest(
  Long productId,
  Double startingPrice,
  Long durationMinutes
) {
  
}
