package com.emi.biddingengine.serviceImpl;

import org.springframework.stereotype.Service;

import com.emi.biddingengine.dtos.BidRequest;
import com.emi.biddingengine.dtos.BidResults;
import com.emi.biddingengine.dtos.BidStats;
import com.emi.biddingengine.repository.AuctionRepository;
import com.emi.biddingengine.repository.BidRepository;
import com.emi.biddingengine.services.BidService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OptmisticLockingStrategy implements BidService {

  private final BidRepository bidRepository;
  private final AuctionRepository auctionRepository;

  @Override
  public BidResults placeBid(BidRequest request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'placeBid'");
  }

  @Override
  public BidStats getAuctionStats(Long auctionId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAuctionStats'");
  }
  
}
