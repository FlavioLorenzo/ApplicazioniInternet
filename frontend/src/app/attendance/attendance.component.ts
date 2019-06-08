import { Component, OnInit } from '@angular/core';
import {RIDES} from './mock-buslines';
import {User} from '../Models/User';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  rides = RIDES;
  index = 0;
  ride = this.rides[this.index];
  pageNumber = this.rides.length;

  constructor() { }

  ngOnInit() {
  }

  pickOrUnpick(user: User) {
    user.picked = !user.picked;
  }

  changePage(event) {
    this.ride = this.rides[event.pageIndex];
  }
}
