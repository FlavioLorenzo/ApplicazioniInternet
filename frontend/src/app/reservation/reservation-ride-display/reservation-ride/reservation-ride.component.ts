import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ReservationPostBody, ReservationsService} from '../../../services/reservations.service';
import {MatDialog} from '@angular/material';
import {ModifyStopPopupComponent} from '../modify-stop-popup/modify-stop-popup.component';
import {RidesContainer, RideWithReservation} from '../../rides-container';
import {DateUtilsService} from '../../../services/date-utils.service';

@Component({
  selector: 'app-reservation-ride',
  templateUrl: './reservation-ride.component.html',
  styleUrls: ['./reservation-ride.component.css']
})
export class ReservationRideComponent implements OnInit {
  @Input() ride: RidesContainer;
  @Output() reservationChange = new EventEmitter<any>();

  constructor(private dialog: MatDialog, private reservationService: ReservationsService,
              private dateUtil: DateUtilsService) { }

  ngOnInit() {}

  isInProgress(back: boolean) {
    return (back && this.ride.backRide.rideBookingStatus === 'In Progress') ||
      (!back && this.ride.forthRide.rideBookingStatus === 'In Progress');
  }

  getLatestPosition(back: boolean) {
    return back ? this.ride.backRide.latestStop : this.ride.forthRide.latestStop;
  }

  getLatestPositionArrivalTime(back: boolean) {
    return back ? this.ride.backRide.latestStopTime : this.ride.forthRide.latestStopTime;
  }

  hasReservation(back: boolean) {
    if (back && this.ride.backRide.hasOwnProperty('reservation')) {
      return true;
    } else if (!back && this.ride.forthRide.hasOwnProperty('reservation')) {
      return true;
    }

    return false;
  }

  getReservationStop(back: boolean): string {
    if (back && this.ride.backRide.hasOwnProperty('reservation')) {
      return this.ride.backRide.reservation.stopName;
    } else if (!back && this.ride.forthRide.hasOwnProperty('reservation')) {
      return this.ride.forthRide.reservation.stopName;
    } else {
      return '---';
    }
  }

  getReservationTime(back: boolean) {
    if (back && this.ride.backRide.hasOwnProperty('reservation')) {
      return this.ride.backRide.reservation.arrivalTime;
    } else if (!back && this.ride.forthRide.hasOwnProperty('reservation')) {
      return this.ride.forthRide.reservation.arrivalTime;
    } else {
      return '---';
    }
  }

  getStatusClass(back: boolean) {
    status = back ? this.ride.backRide.rideBookingStatus : this.ride.forthRide.rideBookingStatus;
    if (status === 'Terminated') {
      return 'terminated-signal';
    } else if (status === 'In Progress') {
      return 'progress-signal';
    } else {
      return 'inactive-signal';
    }
  }

  getSelectedClass(back: boolean) {
    if (this.hasReservation(back)) {
      return 'ai-selected-button-class';
    }
    return '';
  }

  onElementAreaClicked(back: boolean) {
    if (this.hasReservation(back)) {
      this.deleteReservation(back);
    } else {
      this.createReservation(back);
    }
  }

  createReservation(back: boolean) {
    const toSave: RideWithReservation = back ? this.ride.backRide : this.ride.forthRide;
    const rpb: ReservationPostBody = {
      id_child: 1,
      id_stop: -1,
      direction: toSave.direction,
      presence: false
    };

    this.reservationService.createReservation(toSave.lineId, this.dateUtil.fromPrintFormatToSendFormat(toSave.date), rpb).subscribe(
      (data) => {
        console.log(data);
        this.reservationChange.emit(data);
      },
      (error) => {
        console.log(error);
      });
  }

  deleteReservation(back: boolean) {
    const toDelete: RideWithReservation = back ? this.ride.backRide : this.ride.forthRide;

    this.reservationService.deleteReservation(
      toDelete.lineId,
      this.dateUtil.fromPrintFormatToSendFormat(toDelete.date),
      toDelete.reservation.reservationId
    ).subscribe(
      (data) => {
        this.reservationChange.emit(data);
      },
      (error) => {
        console.log(error);
      });
  }

  onModifyReservationDetailAreaClick(event: Event, back: boolean) {
    if (this.hasReservation(back)) {
      event.stopPropagation();

      const toModify: RideWithReservation = back ? this.ride.backRide : this.ride.forthRide;

      const dialogRef = this.dialog.open(ModifyStopPopupComponent, {
        width: '250px',
        data: {ride: toModify}
      });

      dialogRef.afterClosed().subscribe(() => {
        console.log('Closed dialog...');
      });

      dialogRef.componentInstance.update.subscribe(busStop => {
        const rpb: ReservationPostBody = {
          id_child: 1, // TODO: Same story as before... we need to be able to select the kid
          id_stop: busStop,
          direction: toModify.direction,
          presence: false
        };

        this.reservationService.modifyReservation(
          toModify.lineId,
          this.dateUtil.fromPrintFormatToSendFormat(toModify.date),
          toModify.reservation.reservationId,
          rpb
        ).subscribe(
          (data) => {
            this.reservationChange.emit(data);
          },
          (error) => {
            console.log(error);
          });
      });
    }
  }
}
