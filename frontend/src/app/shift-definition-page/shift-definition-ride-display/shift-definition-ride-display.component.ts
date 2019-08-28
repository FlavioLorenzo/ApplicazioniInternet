import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {forkJoin} from 'rxjs';
import {LinesService} from '../../services/lines.service';
import {StopService} from '../../services/stop.service';
import {AvailabilityService} from '../../services/availability.service';
import {RidesWithShiftAvailabilityContainer, ShiftAvailabilityOfRide} from '../rides-with-shift-availability-container';

@Component({
  selector: 'app-shift-definition-ride-display',
  templateUrl: './shift-definition-ride-display.component.html',
  styleUrls: ['./shift-definition-ride-display.component.css']
})
export class ShiftDefinitionRideDisplayComponent implements OnInit {

  lineId: number;
  lineName: string;
  fromDate: string;
  rides: any;
  ridesContainer: Array<RidesWithShiftAvailabilityContainer> = [];

  constructor(private route: ActivatedRoute, private router: Router,
              private lineService: LinesService, private stopService: StopService,
              private availabilityService: AvailabilityService) { }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.lineId = params.id;
      this.fromDate = params.from;
    });

    this.getRidesAndAvailabilities();
  }

  getRidesAndAvailabilities() {
    forkJoin([
      this.lineService
        .getLineNameFromId(
          this.lineId
        ),
      this.stopService
        .getNRidesFromDate(
          this.lineId,
          this.fromDate,
          7
        ),
      this.availabilityService
        .getNAvailabilitiesByUserFromDate(
          this.lineId,
          this.fromDate,
          7
        )
    ]).subscribe(
      (data) => {
        this.lineName = data[0].name;
        this.buildShiftContainer(data[1], data[2]);
      },
      (error) => {console.log(error); },
      () => console.log('Done building data structure')
    );
  }

  buildShiftContainer(rides, availabilities) {
    for (let i = 0, len = rides.length; i < len; i++) {
      const pos = this.getPositionInContainer(this.ridesContainer, rides[i].date);
      const availability = this.getAvailabilitiesInList(availabilities, rides[i].id);
      const toAdd = rides[i];

      if (availability != null) {
        toAdd.availability = availability;
      }

      if (pos > -1) {
        if (toAdd.direction === false) {
          this.ridesContainer[pos].forthRide = toAdd;
        } else {
          this.ridesContainer[pos].backRide = toAdd;
        }
      } else {
        const newRide: RidesWithShiftAvailabilityContainer = {
          date: toAdd.date,
          forthRide: undefined,
          backRide: undefined
        };

        if (toAdd.direction === false) {
          newRide.forthRide = toAdd;
        } else {
          newRide.backRide = toAdd;
        }

        this.ridesContainer.push(newRide);
      }
    }
  }

  getPositionInContainer(ridesContainer: Array<RidesWithShiftAvailabilityContainer>, date: string): number {
    for (let i = 0; i < ridesContainer.length; i++) {
      if (ridesContainer[i].date === date) {
        return i;
      }
    }

    return -1;
  }

  getAvailabilitiesInList(availabilities, rideId): ShiftAvailabilityOfRide {
    // tslint:disable-next-line:prefer-for-of
    for (let i = 0; i < availabilities.length; i++) {
      if (availabilities[i].rideId === rideId) {
        return new ShiftAvailabilityOfRide(
          availabilities[i].id,
          availabilities[i].stopId,
          availabilities[i].stopName,
          availabilities[i].arrivalTime,
          availabilities[i].shiftStatusCode);
      }
    }
    return null;
  }

  onBackClick() {
    this.router.navigate(['shift-definition']);
  }

  onAvailabilityChanged(data) {
    this.getRidesAndAvailabilities();
  }
}
