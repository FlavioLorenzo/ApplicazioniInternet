import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-date-line-selection-page',
  templateUrl: './date-line-selection-page.component.html',
  styleUrls: ['./date-line-selection-page.component.css']
})
export class DateLineSelectionPageComponent implements OnInit {

  @Input() showChild = false;
  @Input() title = '';
  @Output() buttonClicked = new EventEmitter<any>();
  childId: number;
  lineId: number;
  dateSelected: string;

  constructor(private router: Router ) { }

  ngOnInit() {
    this.lineId = null;
    this.dateSelected = null;
  }

  onChildSelected(value: number) {
    this.childId = value;
  }

  onLineSelected(value: number) {
    this.lineId = value;
  }

  onDateChanged(value: string) {
    this.dateSelected = value;
  }

  onButtonClicked() {
    if ( this.lineId !== null && this.dateSelected !== null) {
      if ( this.showChild && this.childId !== null) {
        this.buttonClicked.emit({child: this.childId, line: this.lineId, date: this.dateSelected});
      } else {
        this.buttonClicked.emit({line: this.lineId, date: this.dateSelected});
      }
    }
  }
}
