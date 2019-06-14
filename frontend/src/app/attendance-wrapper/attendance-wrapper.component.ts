import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-attendance-wrapper',
  templateUrl: './attendance-wrapper.component.html',
  styleUrls: ['./attendance-wrapper.component.css']
})
export class AttendanceWrapperComponent implements OnInit {

  selectedDate = new Date();

  constructor() { }

  ngOnInit() {
  }

}
