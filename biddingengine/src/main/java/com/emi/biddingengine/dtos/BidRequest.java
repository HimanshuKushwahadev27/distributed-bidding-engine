package com.emi.biddingengine.dtos;

public record BidRequest(
  Long auctionId,
  Long userId,
  Double amount
) {
  
}
