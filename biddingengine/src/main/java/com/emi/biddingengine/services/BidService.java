package com.emi.biddingengine.services;

import com.emi.biddingengine.dtos.BidRequest;
import com.emi.biddingengine.dtos.BidResults;
import com.emi.biddingengine.dtos.BidStats;

public interface BidService {
  BidResults placeBid(BidRequest request);
  BidStats getAuctionStats(Long auctionId);
}
