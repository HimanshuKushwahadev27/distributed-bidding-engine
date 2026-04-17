package com.emi.biddingengine.dtos;

public record AuctionResponse(
  Long id,
  Long productId,
  Double startingPrice,
  Double currentHighestBid,
  Long highestBidderId,
  String startTime,
  String endTime,
  boolean active
) {
  
}
