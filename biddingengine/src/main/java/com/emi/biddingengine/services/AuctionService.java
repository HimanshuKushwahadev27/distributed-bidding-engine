package com.emi.biddingengine.services;

import java.util.List;

import com.emi.biddingengine.dtos.AuctionRequest;
import com.emi.biddingengine.dtos.AuctionResponse;

public interface AuctionService {

  String createAuction(AuctionRequest request);
  List<AuctionResponse> getAllAuctions();
  AuctionResponse getAuctionById(Long id);
  String closeAuction(Long id);
}
