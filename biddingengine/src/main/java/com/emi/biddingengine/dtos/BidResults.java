package com.emi.biddingengine.dtos;

public record BidResults(
        String status,
        double attemptedAmount,
        Long userId
) {

    public static BidResults failed(long userId, String message, double attemptedAmount) {
        return new BidResults(message, attemptedAmount, userId);
    }
}