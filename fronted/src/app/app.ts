import { Component } from '@angular/core';

import { BiddingDashboardComponent } from './feature/bidding-dashboard/bidding-dashboard.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [BiddingDashboardComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {}
