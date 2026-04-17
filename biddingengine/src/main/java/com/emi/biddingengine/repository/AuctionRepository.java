package com.emi.biddingengine.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emi.biddingengine.entity.Auction;

public interface AuctionRepository extends JpaRepository<Auction, Long> {
  
}
