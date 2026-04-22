import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { BIDDING_ENGINE_API_BASE_URL } from './api.config';
import { AuctionRequest, AuctionResponse, BidRequest, BidResults, BidStats, BidType } from './bidding.models';

@Injectable({ providedIn: 'root' })
export class BiddingApiService {
  readonly apiBaseUrl = BIDDING_ENGINE_API_BASE_URL;

  constructor(private readonly http: HttpClient) {}

  getAuctions() {
    return this.http.get<AuctionResponse[]>(`${this.apiBaseUrl}/api/auctions/all`);
  }

  createAuction(request: AuctionRequest) {
    return this.http.post(`${this.apiBaseUrl}/api/auctions/create`, request, {
      responseType: 'text' as const,
    });
  }

  closeAuction(auctionId: number) {
    return this.http.post(`${this.apiBaseUrl}/api/auctions/close/${auctionId}`, null, {
      responseType: 'text' as const,
    });
  }

  placeBid(request: BidRequest, type: BidType) {
    return this.http.post<BidResults>(`${this.apiBaseUrl}/api/bids/place/${type}`, request);
  }

  simulateBids(auctionId: number, users: number, type: BidType) {
    return this.http.post<BidResults[]>(`${this.apiBaseUrl}/api/bids/simulate/${auctionId}/${users}/${type}`, null);
  }

  getAuctionStats(auctionId: number, type: BidType) {
    return this.http.get<BidStats>(`${this.apiBaseUrl}/api/bids/stats/${auctionId}/${type}`);
  }
}