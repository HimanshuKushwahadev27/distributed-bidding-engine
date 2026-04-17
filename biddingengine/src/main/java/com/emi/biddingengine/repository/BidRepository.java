package com.emi.biddingengine.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.biddingengine.entity.Bid;

public interface BidRepository extends JpaRepository<Bid, Long> {
  
  List<Bid> findByAuctionId(Long auctionId);  
}
