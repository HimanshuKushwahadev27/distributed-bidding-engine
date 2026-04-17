package com.emi.biddingengine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.emi.biddingengine.dtos.BidRequest;
import com.emi.biddingengine.dtos.BidResults;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class Stimulate {
  
  private final BidService bidService;
  private final ExecutorService executorService;

  public List<BidResults> stimulateNaiveBids(int users, Long auctionId){
    List<Future<BidResults>> futures = new ArrayList<>();

    for(int i = 0 ; i < users; i++){
      Long userId = Long.valueOf(i);

      futures.add(executorService.submit(() -> {
        // Simulate a bid request for the user
        //  replaceable with actual bid request logic
        double bidAmount = 100 + Math.random() * 100; // Random bid amount between 100 and 200
        return bidService.placeBid(new BidRequest(auctionId, userId, bidAmount)); 
      }));
    }

    List<BidResults> results = new ArrayList<>();

    for(Future<BidResults> future : futures){
      Long userId = null;
      double attemptedAmount = 0;
      try {
         results.add(future.get());
         userId = future.get().userId();
         attemptedAmount = future.get().attemptedAmount();
      } catch (InterruptedException | ExecutionException e) {
          Thread.currentThread().interrupt();
          results.add(BidResults.failed(userId, e.getMessage(), attemptedAmount));
      }
    }
    return results;
  }
}
