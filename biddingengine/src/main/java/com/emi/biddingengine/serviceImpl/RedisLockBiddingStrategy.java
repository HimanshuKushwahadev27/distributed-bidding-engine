package com.emi.biddingengine.serviceImpl;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
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


//uses redis for distributed locking to handle concurrent bid placements across multiple instances of the application
@Service("redisLockBiddingStrategy")
@RequiredArgsConstructor
@Slf4j
public class RedisLockBiddingStrategy implements BidService {

  private final BidRepository bidRepository;
  private final AuctionRepository auctionRepository;
  private final RedisTemplate<String, String> redisTemplate;

    @Override
    public BidResults placeBid(BidRequest request) {
      String lockKey = "auction_lock:" + request.auctionId();
      String lockValue = UUID.randomUUID().toString(); //unique value to identify lock owner

      int maxRetries = 3;
      long backOffTime = 200;
      
      for(int i = 0 ; i < maxRetries ; i++){
        boolean lockAcquired = false;
        try{
            lockAcquired = acquireLock(lockKey, lockValue);

            if(lockAcquired){
              return placeBidWithRedisLock(request);
            }

            log.info("Attempt {}/{} - Lock busy for auction {}, backing off {}ms",
                i, maxRetries, request.auctionId(), backOffTime);

            Thread.sleep(backOffTime);
            backOffTime = Math.min(backOffTime * 2, 500);
        }catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          return new BidResults("INTERRUPTED", request.amount(), request.userId(), 0, true);
        } finally {
          if(lockAcquired){
            releaseLock(lockKey, lockValue);
          }
        }
      }

      return new BidResults("LOCK_BUSY", request.amount(), request.userId(), 0, true);
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

    private boolean acquireLock(String lockKey, String lockValue) {
     try{
        Boolean locked = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, lockValue, Duration.ofSeconds(5));
        return locked != null && locked;
        
     } catch (Exception e) {
        log.error("Error occurred while acquiring lock for auction {}", lockKey, e);
        return false;
     }
    }

    private void releaseLock(String lockKey, String lockValue) {
      try{
        String currentValue = redisTemplate.opsForValue().get(lockKey);
        if(lockValue.equals(currentValue)){
          redisTemplate.delete(lockKey);
        }
      } catch (Exception e) {
        log.error("Error occurred while releasing lock for auction {}", lockKey, e);
      }
    }

    private BidResults placeBidWithRedisLock(BidRequest request) {
      //fetch auction details
      var auctionOpt = auctionRepository.findById(request.auctionId());
      if(auctionOpt.isEmpty()){
        return new BidResults("AUCTION_NOT_FOUND", request.amount(), request.userId(), 0, true);
      }

      var auction = auctionOpt.get();
      if(!auction.isActive()){
        return new BidResults("AUCTION_CLOSED", request.amount(), request.userId(), 0, true);
      }

      if(request.amount() <= (auction.getCurrentHighestBid() != null ? auction.getCurrentHighestBid() : auction.getStartingPrice())){
        return new BidResults("BID_TOO_LOW", request.amount(), request.userId(), 0, true);
      }

      //place bid
      var bid = new Bid();
      bid.setAuctionId(request.auctionId());
      bid.setUserId(request.userId());
      bid.setAmount(request.amount());
      bid.setTimestamp(Instant.now());
      
      bidRepository.save(bid);

      //update auction with new highest bid
      auction.setCurrentHighestBid(request.amount());
      auction.setHighestBidderId(request.userId());
      auctionRepository.save(auction);
      

      return new BidResults("SUCCESS", request.amount(), request.userId(),0, false);
    }


  
}
