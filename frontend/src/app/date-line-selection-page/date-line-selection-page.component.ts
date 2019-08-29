import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-date-line-selection-page',
  templateUrl: './date-line-selection-page.component.html',
  styleUrls: ['./date-line-selection-page.component.css']
})
export class DateLineSelectionPageComponent implements OnInit {

  @Output() buttonClicked = new EventEmitter<any>();
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
      this.buttonClicked.emit({line: this.lineId, date: this.dateSelected});
    }
  }
}
