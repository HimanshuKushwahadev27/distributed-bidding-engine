package com.emi.biddingengine.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "auctions")
@NoArgsConstructor
public class Auction {
  
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "auction_seq", sequenceName = "auction_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "starting_price", nullable = false)
    private Double startingPrice;

    @Column(name = "current_hīighest_bid")
    private Double currentHighestBid;

    @Column(name = "highest_bidder_id")
    private Long highestBidderId;

    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @Column(name = "active", nullable = false)
    private boolean active;

    @Version
    @Column(name = "version", nullable = false, columnDefinition = "bigint default 0")
    private Long version;
}
