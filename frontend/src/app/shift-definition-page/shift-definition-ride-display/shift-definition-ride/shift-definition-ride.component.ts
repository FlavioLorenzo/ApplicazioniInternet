import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {MatDialog} from '@angular/material';
import {DateUtilsService} from '../../../services/date-utils.service';
import {ModifyStopPopupComponent} from '../../../modify-stop-popup/modify-stop-popup.component';
import {
  RideWithShiftAvailability,
  RidesWithShiftAvailabilityContainer
} from '../../rides-with-shift-availability-container';
import {AvailabilityPostBody, AvailabilityService} from '../../../services/availability.service';

@Component({
  selector: 'app-shift-definition-ride',
  templateUrl: './shift-definition-ride.component.html',
  styleUrls: ['./shift-definition-ride.component.css']
})
export class ShiftDefinitionRideComponent implements OnInit {
  @Input() ride: RidesWithShiftAvailabilityContainer;
  @Output() availabilityChange = new EventEmitter<any>();

  constructor(private dialog: MatDialog, private availabilityService: AvailabilityService,
              private dateUtil: DateUtilsService) { }

  ngOnInit() {}

  hasAvailability(back: boolean) {
    if (back && this.ride.backRide.hasOwnProperty('availability')) {
      return true;
    } else if (!back && this.ride.forthRide.hasOwnProperty('availability')) {
      return true;
    }

    return false;
  }

  getAvailabilityStop(back: boolean): string {
    if (back && this.ride.backRide.hasOwnProperty('availability')) {
      return this.ride.backRide.availability.stopName;
    } else if (!back && this.ride.forthRide.hasOwnProperty('availability')) {
      return this.ride.forthRide.availability.stopName;
    } else {
      return '---';
    }
  }

  getAvailabilityTime(back: boolean) {
    if (back && this.ride.backRide.hasOwnProperty('availability')) {
      return this.ride.backRide.availability.arrivalTime;
    } else if (!back && this.ride.forthRide.hasOwnProperty('availability')) {
      return this.ride.forthRide.availability.arrivalTime;
    } else {
      return '---';
    }
  }

  getStatusClass(back: boolean) {
    const status = back ? this.ride.backRide.locked : this.ride.forthRide.locked;
    if (status === true) {
      return 'locked-signal';
    } else {
      return 'open-signal';
    }
  }

  getStatus(back: boolean): string {
    const status = back ? this.ride.backRide.locked : this.ride.forthRide.locked;
    return status === true ? 'Locked' : 'Open';
  }

  getAvailabilityStatusClass(back: boolean) {
    const status = back ? this.ride.backRide.availability.shiftStatus : this.ride.forthRide.availability.shiftStatus;
    if (status === 1) {
      return 'new-signal';
    } else {
      return 'confirmed-signal';
    }
  }

  getAvailabilityStatus(back: boolean) {
    const status = back ? this.ride.backRide.availability.shiftStatus : this.ride.forthRide.availability.shiftStatus;
    return status === 1 ? 'New' : 'Confirmed';
  }

  getSelectedClass(back: boolean) {
    if (this.hasAvailability(back)) {
      return 'ai-selected-button-class';
    }
    return '';
  }

  onElementAreaClicked(back: boolean) {
    if (this.getStatus(back) === 'Open') {
      if (this.hasAvailability(back)) {
        this.deleteAvailability(back);
      } else {
        this.createAvailability(back);
      }
    }
  }

  createAvailability(back: boolean) {
    const toSave: RideWithShiftAvailability = back ? this.ride.backRide : this.ride.forthRide;
    const apb: AvailabilityPostBody = {
      userId: -1,
      stopId: -1,
      rideId: toSave.id
    };

    this.availabilityService.createAvailability(apb).subscribe(
      (data) => {
        this.availabilityChange.emit(data);
      },
      (error) => {
        console.log(error);
      });
  }

  deleteAvailability(back: boolean) {
    const toDelete: RideWithShiftAvailability = back ? this.ride.backRide : this.ride.forthRide;

    this.availabilityService.deleteAvailability(
      toDelete.availability.availabilityId
    ).subscribe(
      (data) => {
        this.availabilityChange.emit(data);
      },
      (error) => {
        console.log(error);
      });
  }

  onModifyAvailabilityDetailAreaClick(event: Event, back: boolean) {
    if (this.hasAvailability(back)) {
      event.stopPropagation();

      const toModify: RideWithShiftAvailability = back ? this.ride.backRide : this.ride.forthRide;

      const dialogRef = this.dialog.open(ModifyStopPopupComponent, {
        width: '250px',
        data: {rideId: toModify.id, selectedStop: toModify.availability.stopId}
      });

      dialogRef.afterClosed().subscribe(() => {
        console.log('Closed dialog...');
      });

      dialogRef.componentInstance.update.subscribe(busStop => {
        const apb: AvailabilityPostBody = {
          userId: null,
          stopId: busStop,
          rideId: toModify.id
        };

        this.availabilityService.modifyAvailability(
          toModify.availability.availabilityId,
          apb
        ).subscribe(
          (data) => {
            this.availabilityChange.emit(data);
          },
          (error) => {
            console.log(error);
          });
      });
    }
  }
}
