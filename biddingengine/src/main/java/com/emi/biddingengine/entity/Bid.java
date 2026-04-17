package com.emi.biddingengine.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "bids")
@NoArgsConstructor
public class Bid {
  
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "bid_seq", sequenceName = "bid_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "auction_id", nullable = false )
    private Long auctionId;

    @Column(name = "user_id", nullable = false) 
    private Long userId;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
}
