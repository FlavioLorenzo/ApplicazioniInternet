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
  direction = 0;
  ride = this.rides[this.direction];
  pageNumber = this.rides.length;

  lineId: number;

  isValid: boolean;

  @Input()
  date: Date;


  constructor(private reservationsService: ReservationsService) {
    this.direction = 0;
    // TODO replace with selected value
    this.lineId = 1;
    this.isValid = true;
  }

  ngOnInit() {
    this.queryReservationService();
  }

  queryReservationService() {
    this.isValid = false;
    this.reservationsService
      .getReservationsForLineAndDay(this.lineId,
        this.date.toISOString().split('T')[0])
      .subscribe(
        (data) => {
          this.rides = data;
          this.ride = this.rides[0];
          this.pageNumber = this.rides.length;
          this.direction = 0;
          console.log(JSON.stringify(data));
          this.isValid = true;
        },
        (error) => {
          console.log(error);
          this.isValid = false;
          },
        () => console.log('Done loading reservations')
      );
  }

  pickOrUnpick(user: User) {
    user.picked = !user.picked;
  }

  changePage(event) {
    this.direction = event.pageIndex;
    this.ride = this.rides[this.direction];
  }

  changeLine(event) {
    this.lineId = event;
    this.queryReservationService();
  }

  changeDate(event) {
    this.date = event;
    this.queryReservationService();
  }

}
