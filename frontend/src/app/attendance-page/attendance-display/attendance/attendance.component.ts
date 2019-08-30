import {Component, Input, OnInit} from '@angular/core';
import {RIDES} from './mock-buslines';
import {User} from '../../../Models/User';
import {ReservationPostBody, ReservationsService} from '../../../services/reservations.service';
import {UsersService} from '../../../services/users.service';
import {Ride} from '../../../Models/Ride';
import {BusStop} from '../../../Models/BusLineStop';
import { MatDialog } from '@angular/material/dialog';
import {DialogBoxPickNotBookedUserComponent} from './dialog-box-pick-not-booked-user.component';
import {DateUtilsService} from '../../../services/date-utils.service';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  @Input() date: string;
  @Input() lineId: number;

  rides = RIDES;
  direction = 0;
  ride = this.rides[this.direction];
  pageNumber = this.rides.length;

  allUsers = [];
  remainingUsers = [];
  loadedUsers = false;
  loadedLine = false;

  constructor(private reservationsService: ReservationsService,
              private usersService: UsersService,
              private dateService: DateUtilsService,
              public dialog: MatDialog) {
  }

  ngOnInit() {
    this.queryReservationService();
    this.queryAllUsersService();
  }

  queryReservationService() {
    this.reservationsService
      .getReservationsForLineAndDay(this.lineId,
        this.date)
      .subscribe(
        (data) => {
          this.rides = data;
          this.ride = this.rides[this.direction];
          this.pageNumber = this.rides.length;
          this.loadedLine = true;
          if (this.loadedUsers && this.ride) {
            console.log('Calling processRemainingUsers from query reservations');
            this.processRemainingUsers();
          }
        },
        (error) => {
          console.log(error);
          },
        () => console.log('Done loading reservations')
      );
  }

  pickOrUnpick(ride: Ride, busStop: BusStop, passenger: User) {
      const rpb = new ReservationPostBody(passenger.id_user, busStop.id, !!this.direction, !passenger.picked);

      this.reservationsService.modifyReservation(
        this.lineId,
        this.date,
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
    const bookedUserIds: number[] = new Array();
    this.ride.stopList.forEach(busStop => {
        busStop.passengers.forEach(passenger => {
          bookedUserIds.push(passenger.id_user);
        });
      });

    this.remainingUsers = this.allUsers.filter( (item) => {
      if (bookedUserIds.includes(item.id_user)) {
        return false;
      }
      return true;
    });
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

  openDialogBoxPickNotBookedUser(passenger: any) {
    console.log(JSON.stringify(passenger));
    const dialogRef = this.dialog.open(DialogBoxPickNotBookedUserComponent, {
      width: '250px',
      data: {ride: this.ride, user: passenger, direction: this.direction}
    });

    dialogRef.afterClosed().subscribe(() => {
      console.log('closed dialog...');
    });

    dialogRef.componentInstance.update.subscribe(busStop => {
      const rpb = new ReservationPostBody(
        passenger.id_user,
        busStop.id,
        !!this.direction,
        !passenger.picked);
      console.log('update: ' + JSON.stringify(rpb));

      this.reservationsService.createReservation(
        this.lineId,
        this.date,
        rpb
      ).subscribe(() => {
          passenger.picked = !passenger.picked;
          this.ngOnInit();
        },
        (error) => {
          console.log(error);
        });
    });
  }
}

