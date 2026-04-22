import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
  selector: 'app-response-panel',
  standalone: true,
  imports: [CommonModule, MatCardModule],
  templateUrl: './response-panel.component.html',
  styleUrl: './response-panel.component.scss'
})
export class ResponsePanelComponent {
  @Input() lastResponse = 'No API response yet.';
}