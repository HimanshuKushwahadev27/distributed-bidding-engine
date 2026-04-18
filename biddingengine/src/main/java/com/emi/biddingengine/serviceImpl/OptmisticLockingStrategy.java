package com.emi.biddingengine.serviceImpl;

import java.time.Instant;
import java.util.List;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.emi.biddingengine.dtos.BidRequest;
import com.emi.biddingengine.dtos.BidResults;
import com.emi.biddingengine.dtos.BidStats;
import com.emi.biddingengine.entity.Auction;
import com.emi.biddingengine.entity.Bid;
import com.emi.biddingengine.repository.AuctionRepository;
import com.emi.biddingengine.repository.BidRepository;
import com.emi.biddingengine.services.BidService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("optimisticLockingStrategy")
@RequiredArgsConstructor
public class OptmisticLockingStrategy implements BidService {

  private final BidRepository bidRepository;
  private final AuctionRepository auctionRepository;

  @Override
  public BidResults placeBid(BidRequest request) {
    int retryCount = 3;
    int attempts = 0;
    boolean conflictOccured = false;
    for(int i=0 ; i< retryCount ; i++){
      try {
        attempts++;
        return placeBidWithOptimisticLocking(request, attempts, conflictOccured);
      } catch (ObjectOptimisticLockingFailureException  e) {
        conflictOccured = true;
        log.info("Optimistic lock failure, retrying... Attempt: " + (i+1));
      }
    }
    return new BidResults(
      "FAILED_RETRY",   
       request.amount(),
        request.userId(),
        attempts,
        true
    );

  }

  @Override
  public BidStats getAuctionStats(Long auctionId) {
    List<Bid> bids = bidRepository.findByAuctionId(auctionId);

    Double maxBid = bids.stream().mapToDouble(Bid::getAmount).max().orElse(0);
    Double minBid = bids.stream().mapToDouble(Bid::getAmount).min().orElse(0);
    Double avgBid = bids.stream().mapToDouble(Bid::getAmount).average().orElse(0);

    Auction auction = auctionRepository.findById(auctionId)
        .orElseThrow(() -> new RuntimeException("Auction not found"));

     Double currentHighestBid = auction.getCurrentHighestBid();

     return new BidStats(maxBid, minBid, avgBid, currentHighestBid);
  }


  private void sleep(long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  private BidResults placeBidWithOptimisticLocking(BidRequest request, int attempts, boolean conflictOccurred) {
    Auction auction = auctionRepository.findById(request.auctionId())
        .orElseThrow(() -> new RuntimeException("Auction not found"));
    
    Double currentBid = auction.getCurrentHighestBid();

    sleep(100);

    if(request.amount() > currentBid) {
      // Place the bid
      auction.setCurrentHighestBid(request.amount());
      auction.setHighestBidderId(request.userId());
      auctionRepository.save(auction);
      log.info("Bid placed: " + request.amount() + " by user: " + request.userId());

      Bid bid1 = new Bid();
      bid1.setAuctionId(request.auctionId());
      bid1.setUserId(request.userId());
      bid1.setAmount(request.amount());
      bid1.setTimestamp(Instant.now());
      bidRepository.save(bid1);

      return new BidResults("SUCCESS", request.amount(), request.userId(), attempts, conflictOccurred);
    }

    return new BidResults("FAILED", request.amount(), request.userId(), attempts, conflictOccurred);
  }
  
}
