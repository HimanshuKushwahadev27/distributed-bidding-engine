import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-dashboard-summary',
  standalone: true,
  imports: [CommonModule, MatToolbarModule, MatCardModule, MatButtonModule],
  templateUrl: './dashboard-summary.component.html',
  styleUrl: './dashboard-summary.component.scss'
})
export class DashboardSummaryComponent {
  @Input() auctionsCount = 0;
  @Input() activeAuctionCount = 0;
  @Input() highestCurrentBid = 0;
  @Input() selectedAuctionId: number | null = null;
  @Input() loadingAuctions = false;
  @Input() loadingAction = false;
  @Input() errorMessage: string | null = null;

  @Output() refresh = new EventEmitter<void>();
  @Output() reloadStats = new EventEmitter<void>();
}