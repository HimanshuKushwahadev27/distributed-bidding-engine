import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatListModule } from '@angular/material/list';

import { ActivityItem } from '../../../bidding.models';

@Component({
  selector: 'app-activity-feed',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule, MatDividerModule],
  templateUrl: './activity-feed.component.html',
  styleUrl: './activity-feed.component.scss'
})
export class ActivityFeedComponent {
  @Input() activity: ActivityItem[] = [];
}