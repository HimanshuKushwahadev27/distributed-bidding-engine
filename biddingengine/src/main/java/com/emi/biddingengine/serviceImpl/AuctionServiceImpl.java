package com.emi.biddingengine.serviceImpl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.emi.biddingengine.dtos.AuctionRequest;
import com.emi.biddingengine.dtos.AuctionResponse;
import com.emi.biddingengine.entity.Auction;
import com.emi.biddingengine.repository.AuctionRepository;
import com.emi.biddingengine.services.AuctionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

  private final AuctionRepository auctionRepository;

  @Override
  public String createAuction(AuctionRequest request) {
    Auction auction = new Auction();

    //can call product service to validate productId before creating auction (openfeign client to product service)
    auction.setProductId(request.productId());
    auction.setStartingPrice(request.startingPrice());
    auction.setCurrentHighestBid(request.startingPrice());
    auction.setHighestBidderId(null);
    
    Instant now = Instant.now();
    auction.setStartTime(now);
    auction.setEndTime(now.plusSeconds(request.durationMinutes() * 60));
    auction.setActive(true);
    
    Auction savedAuction = auctionRepository.save(auction);
    return "Auction created successfully with ID: " + savedAuction.getId();
  }

  @Override
  public List<AuctionResponse> getAllAuctions() {
    return auctionRepository.findAll().stream()
        .map(this::convertToResponse)
        .toList();
  }

  @Override
  public AuctionResponse getAuctionById(Long id) {
    return auctionRepository.findById(id)
        .map(this::convertToResponse)
        .orElseThrow(() -> new IllegalArgumentException("Auction not found with ID: " + id));
  }

  @Override
  public String closeAuction(Long id) {
    Auction auction = auctionRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Auction not found with ID: " + id));
    
    auction.setActive(false);
    auctionRepository.save(auction);
    return "Auction closed successfully with ID: " + id;
  }
  
  private AuctionResponse convertToResponse(Auction auction) {
    return new AuctionResponse(
        auction.getId(),
        auction.getProductId(),
        auction.getStartingPrice(),
        auction.getCurrentHighestBid(),
        auction.getHighestBidderId(),
        auction.getStartTime().toString(),
        auction.getEndTime().toString(),
        auction.isActive()
    );
  }
}
