import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReservationPostBody, ReservationsService} from '../../../services/reservations.service';
import {Ride} from '../../../Models/Ride';
import {BusStop} from '../../../Models/BusLineStop';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material';
import {DialogBoxPickNotBookedUserComponent} from './dialog-box-pick-not-booked-user.component';
import {Child} from '../../../Models/Child';
import {ChildrenService} from '../../../services/children.service';
import {DateUtilsService} from '../../../services/date-utils.service';
import {RideService} from '../../../services/ride.service';

@Component({
  selector: 'app-attendance',
  templateUrl: './attendance.component.html',
  styleUrls: ['./attendance.component.css']
})
export class AttendanceComponent implements OnInit {
  ride: Ride;

  @Input('ride')
  set inputRide(ride: Ride) {
    this.ride = ride;
    this.getFreeUsers();
  }
  get inputRide(): Ride { return this.ride; }

  @Output() attendanceChange = new EventEmitter();

  freeChildren: Array<Child> = [];

  constructor(private reservationsService: ReservationsService, private childrenService: ChildrenService,
              public dialog: MatDialog, private dateService: DateUtilsService, private rideService: RideService,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
  }

  getFreeUsers() {
    this.childrenService.getAllFreeChildren(
      this.dateService.fromPrintFormatToSendFormat(this.ride.date),
      this.ride.direction).subscribe(
        (data) => {
        this.freeChildren = data;
      },
      (error) => {console.log(error); },
      () => console.log('Done building free children data structure')
    );
  }

  pickOrUnpick(busStop: BusStop, passenger: Child) {
    const rpb = new ReservationPostBody(passenger.childId, busStop.id, this.ride.direction, !passenger.picked);

    this.reservationsService.modifyReservation(
      this.ride.lineId,
      this.dateService.fromPrintFormatToSendFormat(this.ride.date),
      passenger.reservationId,
      rpb
    ).subscribe((data) => {
        passenger.picked = !passenger.picked;
      },
      (error) => {
        console.log(error);

        if (this.ride.rideBookingStatus === 'Not started yet') {
          this.snackBar.open('Cannot select the child. The ride in not started yet.');
        } else {
          this.snackBar.open('Cannot select the child. Maybe his stop is already closed?');
        }
      });
  }

  openDialogBoxPickNotBookedChild(passenger: Child) {
    const dialogRef = this.dialog.open(DialogBoxPickNotBookedUserComponent, {
      width: '250px',
      data: {ride: this.ride, user: passenger, direction: this.ride.direction}
    });

    dialogRef.afterClosed().subscribe(() => {
      console.log('closed dialog...');
    });

    dialogRef.componentInstance.update.subscribe(busStop => {
      const rpb = new ReservationPostBody(
        passenger.childId,
        busStop.id,
        this.ride.direction,
        !passenger.picked);

      this.reservationsService.createReservation(
        this.ride.lineId,
        this.dateService.fromPrintFormatToSendFormat(this.ride.date),
        rpb
      ).subscribe(() => {
          passenger.picked = !passenger.picked;
          this.attendanceChange.emit();
        },
        (error) => {
          console.log(error);
        });
    });
  }

  // false: open, true:close
  onChangeRideStatus(openClose: boolean) {
    this.rideService.changeRideBookingStatus(openClose, this.ride.id).subscribe(
      (data) => {
        this.ride.rideBookingStatus = data.rideBookingStatus;
        if (this.ride.rideBookingStatus === 'Terminated' ) {
          this.attendanceChange.emit();
        }
      },
      (error) => {
        console.log(error);
      });
  }

  isNextStop(isFirst: boolean, i: number) {
    if ((this.ride.rideBookingStatus !== 'In Progress') || ((this.ride.latestStopId != null) && isFirst)) {
      return false;
    }

    return ((this.ride.latestStopId == null) && isFirst) ||
      ((this.ride.latestStopId != null) && (this.ride.latestStopId === this.ride.stopList[i - 1].id));
  }

  closeStop(id: number) {
    this.rideService.closeStop(this.ride.id, id).subscribe(
      () => { this.attendanceChange.emit(); },
      (error) => { console.log(error); } );
  }
}

