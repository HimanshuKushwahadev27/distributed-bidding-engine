package com.emi.biddingengine.dtos;

public record BidResults(
    String status,
    double attemptedAmount,
    Long userId,
    int attempts,
    boolean conflictOccurred
) {

    public static BidResults failed(long userId, String message, double attemptedAmount, int attempts, boolean conflictOccurred) {
        return new BidResults(message, attemptedAmount, userId, attempts, conflictOccurred);
    }
}