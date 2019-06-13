import { Component, OnInit } from '@angular/core';
import {RIDES} from './mock-buslines';
import {User} from '../Models/User';
import {ReservationsService} from '../services/reservations.service';

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

  date: Date;
  direction: number;
  lineId: number;


  constructor(private reservationsService: ReservationsService) {
    this.date = new Date();
    this.direction = 0;
    // TODO replace with selected value
    this.lineId = 1;
  }

  ngOnInit() {
  }

  pickOrUnpick(user: User) {
    user.picked = !user.picked;
  }

  changePage(event) {
    this.reservationsService
      .getReservationsForLineAndDay(this.lineId,
        this.date.toISOString().split('T')[0])
      .subscribe(bothDirection => this.ride = bothDirection[this.direction]);
    // this.ride = this.rides[event.pageIndex];
  }
}
