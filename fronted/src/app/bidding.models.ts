export type BidType = 'redisLockBiddingStrategy' | 'naiveBiddingStrategy' | 'optimisticLockingStrategy';

export interface StrategyOption {
  value: BidType;
  label: string;
  description: string;
}

export interface ActivityItem {
  tone: 'success' | 'error' | 'info';
  title: string;
  detail: string;
  timestamp: string;
}

export interface CreateAuctionDraft {
  productId: number | null;
  startingPrice: number | null;
  durationMinutes: number | null;
}

export interface PlaceBidDraft {
  auctionId: number | null;
  userId: number | null;
  amount: number | null;
  type: BidType;
}

export interface SimulateDraft {
  auctionId: number | null;
  users: number | null;
  type: BidType;
}

export interface StatsDraft {
  auctionId: number | null;
  type: BidType;
}

export interface AuctionRequest {
  productId: number;
  startingPrice: number;
  durationMinutes: number;
}

export interface AuctionResponse {
  id: number;
  productId: number;
  startingPrice: number;
  currentHighestBid: number | null;
  highestBidderId: number | null;
  startTime: string;
  endTime: string;
  active: boolean;
}

export interface BidRequest {
  auctionId: number;
  userId: number;
  amount: number;
}

export interface BidResults {
  status: string;
  attemptedAmount: number;
  userId: number;
  attempts: number;
  conflictOccurred: boolean;
}

export interface BidStats {
  maxBid: number;
  minBid: number;
  avgBid: number;
  currentHighestBid: number;
}