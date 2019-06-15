import {Component, Input, OnInit} from '@angular/core';
import {RIDES} from './mock-buslines';
import {User} from '../Models/User';
import {ReservationPostBody, ReservationsService} from '../services/reservations.service';
import {UsersService} from '../services/users.service';
import {Ride} from '../Models/Ride';
import {BusStop} from '../Models/BusLineStop';

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

  allUsers = [];
  remainingUsers = [];
  loadedUsers = false;
  loadedLine = false;

  lineId: number;

  isValid: boolean; // If is not valid shows empty screen

  @Input() date: Date; // Linked to calendar component TODO: da rimuovere, in attendance-wrapper.component.html spiego il perchè


  constructor(private reservationsService: ReservationsService, private usersService: UsersService) {
    this.direction = 0;
    // TODO replace with selected value
    this.lineId = 1;
    this.isValid = true;
  }

  ngOnInit() {
    this.queryReservationService();
    this.queryAllUsersService();
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
          this.isValid = true; // TODO: IS VALID DOVREBBE ESSERE SE DATA NON E' VUOTO: QUELLO CHE SUCCEDE ORA
                               // TODO: E' CHE NEL CASO LA RISPOSTA SIA VUOTA VIENE CHIAMATO L'ERRORE. IN REALTÀ L'ERRORE DOVREBBE
                               // TODO: ESSERE CHIAMATO SOLO QUANDO EFFETTIVAMENTE C'È UN ERRORE
          this.loadedLine = true;
          if (this.loadedUsers && this.ride) {
            console.log('Calling processRemainingUsers from query reservations');
            this.processRemainingUsers();
          }
        },
        (error) => {
          console.log(error);
          this.isValid = false;
          },
        () => console.log('Done loading reservations')
      );
  }

  pickOrUnpick(ride: Ride, busStop: BusStop, passenger: User) {
    const rpb = new ReservationPostBody(passenger.userId, busStop.id,
      ride.stopList[ride.stopList.length - 1].id, !!this.direction, !passenger.picked);
    console.log(JSON.stringify(rpb));

    this.reservationsService.modifyReservation(
      this.lineId,
      this.date.toISOString().split('T')[0],
      passenger.reservationId,
      rpb
    ).subscribe((data) => {
      passenger.picked = !passenger.picked;
    },
      (error) => {
        console.log(error);
      });
  }

  changePage(event) {
    this.direction = event.pageIndex;
    this.ride = this.rides[this.direction];
    this.processRemainingUsers();
  }

  changeLine(event) {
    this.lineId = event;
    this.queryReservationService();
  }

  changeDate(event) {
    this.date = event;
    this.queryReservationService();
  }

  private processRemainingUsers() {
    const actualUsers = this.allUsers.slice();
    this.ride.stopList.forEach(busStop => {
      busStop.passengers.forEach(passenger => {
        console.log(passenger.username);
        const index = this.allUsers.findIndex(pass => pass.first_name === passenger.username);
        console.log('INDEX FOUND:', index);
        if (index > -1) {
          actualUsers.splice(index, 1);
        }
      });
    });

    this.remainingUsers = actualUsers;

  }

  private queryAllUsersService() {

    this.usersService.getAllusers().subscribe(
      (data) => {
        this.allUsers = data;
        this.loadedUsers = true;
        if (this.loadedLine  && this.ride) {
          console.log('Calling processRemainingUsers from query users');
          this.processRemainingUsers();
        }
      },
    (error) => {},
      () => console.log('Loading users completed')
    );
  }
}
