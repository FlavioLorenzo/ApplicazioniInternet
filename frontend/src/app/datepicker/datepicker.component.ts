import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDatepickerInputEvent} from '@angular/material';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.css'],
  providers: [DatePipe]
})
export class DatepickerComponent implements OnInit {
  @Input() placeholderText = 'Select the date';
  @Input() fromDate: Date = null;
  @Output() dateChanged = new EventEmitter<string>();

  constructor(private datePipe: DatePipe) { }

  ngOnInit() {
  }

  onChangeDateEvent(event: MatDatepickerInputEvent<Date>) {
    this.dateChanged.emit(this.datePipe.transform(event.value, 'yyyy-MM-dd'));
  }

  myFilter = (d: Date): boolean => {
    if (this.fromDate === null) {
      return true;
    }

    return d >= this.fromDate;
  }
}
