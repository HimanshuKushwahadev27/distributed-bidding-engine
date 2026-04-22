import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, computed, OnInit, signal } from '@angular/core';

import { BiddingApiService } from '../../bidding-api.service';
import {
  ActivityItem,
  AuctionRequest,
  AuctionResponse,
  BidRequest,
  BidStats,
  BidType,
  CreateAuctionDraft,
  PlaceBidDraft,
  SimulateDraft,
  StatsDraft,
  StrategyOption,
} from '../../bidding.models';
import { ActivityFeedComponent } from '../../shared/components/activity-feed/activity-feed.component';
import { AuctionActionsComponent } from '../../shared/components/auction-actions/auction-actions.component';
import { AuctionInspectorComponent } from '../../shared/components/auction-inspector/auction-inspector.component';
import { AuctionListComponent } from '../../shared/components/auction-list/auction-list.component';
import { DashboardSummaryComponent } from '../../shared/components/dashboard-summary/dashboard-summary.component';
import { ResponsePanelComponent } from '../../shared/components/response-panel/response-panel.component';

const DEFAULT_CREATE_AUCTION: CreateAuctionDraft = {
  productId: 11,
  startingPrice: 95,
  durationMinutes: 180,
};

const DEFAULT_PLACE_BID: PlaceBidDraft = {
  auctionId: null,
  userId: null,
  amount: null,
  type: 'naiveBiddingStrategy',
};

const DEFAULT_SIMULATE: SimulateDraft = {
  auctionId: null,
  users: 5,
  type: 'naiveBiddingStrategy',
};

const DEFAULT_STATS: StatsDraft = {
  auctionId: null,
  type: 'naiveBiddingStrategy',
};

@Component({
  selector: 'app-bidding-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    ActivityFeedComponent,
    AuctionActionsComponent,
    AuctionInspectorComponent,
    AuctionListComponent,
    DashboardSummaryComponent,
    ResponsePanelComponent,
  ],
  templateUrl: './bidding-dashboard.component.html',
  styleUrl: './bidding-dashboard.component.scss'
})
export class BiddingDashboardComponent implements OnInit {
  readonly strategyOptions: StrategyOption[] = [
    {
      value: 'naiveBiddingStrategy',
      label: 'Naive strategy',
      description: 'Simple compare-and-save flow for basic load testing.',
    },
    {
      value: 'optimisticLockingStrategy',
      label: 'Optimistic locking',
      description: 'Safer strategy for concurrent bid handling.',
    },
    {
      value: 'redisLockBiddingStrategy',
      label: 'Redis lock',
      description: 'Redis-based distributed lock for maximum safety under high contention.',
    }
  ];

  readonly auctions = signal<AuctionResponse[]>([]);
  readonly selectedAuctionId = signal<number | null>(null);
  readonly selectedStats = signal<BidStats | null>(null);
  readonly lastResponse = signal<string>('No API response yet.');
  readonly activity = signal<ActivityItem[]>([]);
  readonly loadingAuctions = signal(false);
  readonly loadingAction = signal(false);
  readonly errorMessage = signal<string | null>(null);

  createAuctionModel: CreateAuctionDraft = { ...DEFAULT_CREATE_AUCTION };
  placeBidModel: PlaceBidDraft = { ...DEFAULT_PLACE_BID };
  simulateModel: SimulateDraft = { ...DEFAULT_SIMULATE };
  statsModel: StatsDraft = { ...DEFAULT_STATS };

  readonly activeAuctionCount = computed(() => this.auctions().filter((auction) => auction.active).length);
  readonly highestCurrentBid = computed(() => {
    const values = this.auctions().map((auction) => auction.currentHighestBid ?? auction.startingPrice);
    return values.length ? Math.max(...values) : 0;
  });
  readonly selectedAuction = computed(() => this.auctions().find((auction) => auction.id === this.selectedAuctionId()) ?? null);

  constructor(private readonly api: BiddingApiService) {}

  ngOnInit(): void {
    this.refreshAuctions(true);
  }

  refreshAuctions(selectFirstIfNeeded = false): void {
    this.loadingAuctions.set(true);
    this.api.getAuctions().subscribe({
      next: (auctions) => {
        const sorted = [...auctions].sort((left, right) => right.id - left.id);
        this.auctions.set(sorted);

        if (!sorted.length) {
          this.selectedAuctionId.set(null);
          this.selectedStats.set(null);
          this.syncDraftsToSelection(null);
          this.lastResponse.set('No auctions were returned by the API.');
          this.pushActivity('info', 'Auctions refreshed', 'The API returned an empty list.');
          return;
        }

        const currentSelection = this.selectedAuctionId();
        const selectionExists = currentSelection !== null && sorted.some((auction) => auction.id === currentSelection);
        let nextSelection = currentSelection;
        let shouldLoadStats = false;

        if (!selectionExists || (selectFirstIfNeeded && sorted.length > 0)) {
          nextSelection = sorted[0].id;
          shouldLoadStats = true;
        }

        if (nextSelection !== null) {
          this.syncDraftsToSelection(nextSelection);

          if (nextSelection !== currentSelection) {
            this.pushActivity('info', 'Selection updated', `Focused auction #${nextSelection}.`);
          }

          if (shouldLoadStats) {
            this.loadStats(nextSelection, false);
          }
        }

        this.pushActivity('success', 'Auctions refreshed', `${sorted.length} auctions loaded from Railway.`);
      },
      error: (error) => this.handleLoadError('Failed to load auctions', error),
      complete: () => this.loadingAuctions.set(false),
    });
  }

  selectAuction(auctionId: number): void {
    this.selectedAuctionId.set(auctionId);
    this.syncDraftsToSelection(auctionId);
    this.loadStats(auctionId);
  }

  setStatsType(type: BidType): void {
    this.statsModel.type = type;

    if (this.selectedAuctionId() !== null) {
      this.loadStats(this.selectedAuctionId(), false);
    }
  }

  createAuction(request: CreateAuctionDraft): void {
    const payload: AuctionRequest = {
      productId: Number(request.productId),
      startingPrice: Number(request.startingPrice),
      durationMinutes: Number(request.durationMinutes),
    };

    this.beginAction();
    this.api.createAuction(payload).subscribe({
      next: (response) => {
        this.setLastResponse(response);
        this.pushActivity('success', 'Auction created', response);
        this.loadingAction.set(false);
        this.createAuctionModel = { ...DEFAULT_CREATE_AUCTION };
        this.refreshAuctions(true);
      },
      error: (error) => this.handleActionError('Failed to create auction', error),
    });
  }

  placeBid(request: PlaceBidDraft): void {
    const payload: BidRequest = {
      auctionId: Number(request.auctionId),
      userId: Number(request.userId),
      amount: Number(request.amount),
    };

    this.beginAction();
    this.api.placeBid(payload, request.type).subscribe({
      next: (response) => {
        this.setLastResponse(response);
        this.pushActivity('success', 'Bid placed', `User #${payload.userId} bid ${payload.amount} on auction #${payload.auctionId}.`);
        this.loadingAction.set(false);
        this.placeBidModel = { ...DEFAULT_PLACE_BID, auctionId: this.selectedAuctionId(), type: request.type };
        this.refreshAuctions(false);
      },
      error: (error) => this.handleActionError('Failed to place bid', error),
    });
  }

  simulateBids(request: SimulateDraft): void {
    const auctionId = Number(request.auctionId);
    const users = Number(request.users);

    this.beginAction();
    this.api.simulateBids(auctionId, users, request.type).subscribe({
      next: (response) => {
        this.setLastResponse(response);
        this.pushActivity('success', 'Simulation finished', `${response.length} bids simulated on auction #${auctionId}.`);
        this.loadingAction.set(false);
        this.simulateModel = { ...DEFAULT_SIMULATE, auctionId: this.selectedAuctionId(), type: request.type };
        this.refreshAuctions(false);
      },
      error: (error) => this.handleActionError('Failed to simulate bids', error),
    });
  }

  loadStats(auctionId?: number | null, announce = true): void {
    const targetAuctionId = auctionId ?? this.statsModel.auctionId ?? this.selectedAuctionId();

    if (targetAuctionId === null) {
      this.errorMessage.set('Select an auction before loading stats.');
      return;
    }

    this.statsModel.auctionId = targetAuctionId;
    this.beginAction();
    this.api.getAuctionStats(targetAuctionId, this.statsModel.type).subscribe({
      next: (stats) => {
        this.selectedStats.set(stats);
        this.setLastResponse(stats);
        if (announce) {
          this.pushActivity('success', 'Stats loaded', `Loaded stats for auction #${targetAuctionId} using ${this.statsModel.type}.`);
        }
        this.loadingAction.set(false);
      },
      error: (error) => this.handleActionError('Failed to load auction stats', error),
    });
  }

  closeAuction(auction: AuctionResponse): void {
    if (!auction.active) {
      return;
    }

    if (!globalThis.confirm(`Close auction #${auction.id}?`)) {
      return;
    }

    this.beginAction();
    this.api.closeAuction(auction.id).subscribe({
      next: (response) => {
        this.setLastResponse(response);
        this.pushActivity('info', 'Auction closed', response);
        this.loadingAction.set(false);
        this.refreshAuctions(false);
      },
      error: (error) => this.handleActionError('Failed to close auction', error),
    });
  }

  trackAuction(_: number, auction: AuctionResponse): number {
    return auction.id;
  }

  private beginAction(): void {
    this.loadingAction.set(true);
    this.errorMessage.set(null);
  }

  private handleLoadError(context: string, error: unknown): void {
    this.loadingAuctions.set(false);
    this.errorMessage.set(`${context}: ${this.describeError(error)}`);
    this.setLastResponse(`Error: ${this.describeError(error)}`);
    this.pushActivity('error', context, this.describeError(error));
  }

  private handleActionError(context: string, error: unknown): void {
    this.loadingAction.set(false);
    this.errorMessage.set(`${context}: ${this.describeError(error)}`);
    this.setLastResponse(`Error: ${this.describeError(error)}`);
    this.pushActivity('error', context, this.describeError(error));
  }

  private describeError(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const body = error.error;

      if (typeof body === 'string' && body.trim().length > 0) {
        return body;
      }

      if (body && typeof body === 'object') {
        const structuredBody = body as { message?: unknown; error?: unknown; title?: unknown };
        const message = structuredBody.message ?? structuredBody.error ?? structuredBody.title;

        if (typeof message === 'string' && message.trim().length > 0) {
          return message;
        }
      }

      return error.message || `HTTP ${error.status}`;
    }

    if (error instanceof Error) {
      return error.message;
    }

    if (typeof error === 'string') {
      return error;
    }

    return 'Unknown error';
  }

  private setLastResponse(value: unknown): void {
    this.lastResponse.set(typeof value === 'string' ? value : JSON.stringify(value, null, 2));
  }

  private pushActivity(tone: ActivityItem['tone'], title: string, detail: string): void {
    this.activity.update((items) => [
      {
        tone,
        title,
        detail,
        timestamp: new Date().toLocaleTimeString(),
      },
      ...items,
    ].slice(0, 8));
  }

  private syncDraftsToSelection(auctionId: number | null): void {
    this.placeBidModel.auctionId = auctionId;
    this.simulateModel.auctionId = auctionId;
    this.statsModel.auctionId = auctionId;
  }
}