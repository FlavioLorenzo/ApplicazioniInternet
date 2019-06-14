import {Component, Input, OnInit} from '@angular/core';
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

  direction: number;
  lineId: number;

  @Input()
  date: Date;


  constructor(private reservationsService: ReservationsService) {
    this.direction = 0;
    // TODO replace with selected value
    this.lineId = 1;
  }

  ngOnInit() {
    this.queryReservationService();
  }

  queryReservationService() {
    this.reservationsService
      .getReservationsForLineAndDay(this.lineId,
        this.date.toISOString().split('T')[0])
      .subscribe(
        (data) => {
          this.rides = data;
          this.ride = this.rides[0];
          this.pageNumber = this.rides.length;
          this.index = 0;
          console.log(JSON.stringify(data));
        },
        (error) => {  console.log(error); },
        () => console.log('Done loading reservations')
      );
  }

  pickOrUnpick(user: User) {
    user.picked = !user.picked;
  }

  changePage(event) {
    this.index = event.pageIndex;
    this.ride = this.rides[this.index];
  }

  changeLine(event) {
    this.lineId = event;
    this.queryReservationService();
  }

}
