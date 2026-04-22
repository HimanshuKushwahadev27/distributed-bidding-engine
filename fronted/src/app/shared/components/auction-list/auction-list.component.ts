import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatChipsModule } from '@angular/material/chips';
import { MatTableModule } from '@angular/material/table';

import { AuctionResponse } from '../../../bidding.models';

@Component({
  selector: 'app-auction-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatTableModule, MatButtonModule, MatChipsModule],
  templateUrl: './auction-list.component.html',
  styleUrl: './auction-list.component.scss'
})
export class AuctionListComponent {
  @Input() auctions: AuctionResponse[] = [];
  @Input() selectedAuctionId: number | null = null;
  @Input() loadingAction = false;

  @Output() selectAuction = new EventEmitter<number>();
  @Output() closeAuction = new EventEmitter<AuctionResponse>();

  readonly displayedColumns = ['id', 'product', 'starting', 'current', 'bidder', 'ends', 'status', 'actions'];

  trackById(_: number, auction: AuctionResponse): number {
    return auction.id;
  }
}