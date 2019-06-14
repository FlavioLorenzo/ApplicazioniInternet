import {Component, Input, OnInit} from '@angular/core';
import {AttendanceComponent} from '../attendance/attendance.component';
import {MatDatepickerInputEvent} from '@angular/material';

@Component({
  selector: 'app-datepicker',
  templateUrl: './datepicker.component.html',
  styleUrls: ['./datepicker.component.css']
})
export class DatepickerComponent implements OnInit {

  @Input() attendance: AttendanceComponent;

  constructor() { }

  ngOnInit() {
  }

  changeDateEvent(event: MatDatepickerInputEvent<Date>) {
    this.attendance.changeDate(event.value);
    console.log('event' + event.value.toISOString().split('T')[0]);
  }
}
