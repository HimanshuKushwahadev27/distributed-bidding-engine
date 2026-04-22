import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';

import { AuctionResponse, BidStats, BidType, StrategyOption } from '../../../bidding.models';

@Component({
  selector: 'app-auction-inspector',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatDividerModule, MatFormFieldModule, MatSelectModule, MatButtonModule],
  templateUrl: './auction-inspector.component.html',
  styleUrl: './auction-inspector.component.scss'
})
export class AuctionInspectorComponent {
  @Input() selectedAuction: AuctionResponse | null = null;
  @Input() selectedStats: BidStats | null = null;
  @Input() statsType: BidType = 'naiveBiddingStrategy';
  @Input() strategyOptions: StrategyOption[] = [];
  @Input() loadingAction = false;

  @Output() statsTypeChange = new EventEmitter<BidType>();
  @Output() loadStats = new EventEmitter<number | null | undefined>();
}