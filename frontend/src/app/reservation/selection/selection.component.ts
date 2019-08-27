import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {
  lineId: number;
  dateSelected: string;

  constructor(private router: Router ) { }

  ngOnInit() {
    this.lineId = null;
    this.dateSelected = null;
  }

  onLineSelected(value: number) {
    this.lineId = value;
  }

  onDateChanged(value: string) {
    this.dateSelected = value;
  }

  onButtonClicked() {
    if ( this.lineId !== null && this.dateSelected !== null) {
      this.router.navigate(['/reservation', 'line', this.lineId, this.dateSelected]);
    }
  }
}
