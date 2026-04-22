import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';

import {
  CreateAuctionDraft,
  PlaceBidDraft,
  SimulateDraft,
  StrategyOption,
} from '../../../bidding.models';

@Component({
  selector: 'app-auction-actions',
  standalone: true,
  imports: [CommonModule, FormsModule, MatCardModule, MatTabsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './auction-actions.component.html',
  styleUrl: './auction-actions.component.scss'
})
export class AuctionActionsComponent {
  @Input({ required: true }) createModel!: CreateAuctionDraft;
  @Input({ required: true }) placeBidModel!: PlaceBidDraft;
  @Input({ required: true }) simulateModel!: SimulateDraft;
  @Input({ required: true }) strategyOptions: StrategyOption[] = [];
  @Input() selectedAuctionId: number | null = null;
  @Input() loadingAction = false;

  @Output() createAuction = new EventEmitter<CreateAuctionDraft>();
  @Output() placeBid = new EventEmitter<PlaceBidDraft>();
  @Output() simulateBids = new EventEmitter<SimulateDraft>();

  submitCreate(form: NgForm): void {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    this.createAuction.emit({ ...this.createModel });
  }

  submitBid(form: NgForm): void {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    this.placeBid.emit({ ...this.placeBidModel });
  }

  submitSimulation(form: NgForm): void {
    if (form.invalid) {
      form.control.markAllAsTouched();
      return;
    }

    this.simulateBids.emit({ ...this.simulateModel });
  }
}