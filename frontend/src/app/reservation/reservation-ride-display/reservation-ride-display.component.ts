import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {StopService} from '../../services/stop.service';
import {ReservationsService} from '../../services/reservations.service';
import {LinesService} from '../../services/lines.service';
import {forkJoin} from 'rxjs';
import {ReservationOfRide, RidesWithReservationContainer} from '../rides-with-reservation-container';

@Component({
  selector: 'app-reservation-ride-display',
  templateUrl: './reservation-ride-display.component.html',
  styleUrls: ['./reservation-ride-display.component.css']
})
export class ReservationRideDisplayComponent implements OnInit {
  lineId: number;
  lineName: string;
  fromDate: string;
  rides: any;
  ridesContainer: Array<RidesWithReservationContainer> = [];

  constructor(private route: ActivatedRoute, private router: Router,
              private stopService: StopService, private reservationService: ReservationsService,
              private lineService: LinesService) { }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.lineId = params.id;
      this.fromDate = params.from;
    });

    this.getRidesAndReservations();
  }

  getRidesAndReservations() {
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
      this.reservationService
        .getNReservationsByChildFromDate(
          this.lineId,
          1, // TODO: The child must be given as an argument to this component
          this.fromDate,
          7
        )
    ]).subscribe(
      (data) => {
        this.lineName = data[0].name;
        this.buildRidesContainer(data[1], data[2]);
      },
      (error) => {console.log(error); },
      () => console.log('Done building data structure')
    );
  }

  buildRidesContainer(rides, res) {
    for (let i = 0, len = rides.length; i < len; i++) {
      let pos = this.getPositionInContainer(this.ridesContainer, rides[i].date);
      let reservation = this.getReservationInList(res, rides[i].id);
      let toAdd = rides[i];

      if (reservation != null) {
        toAdd.reservation = reservation;
      }

      if (pos > -1) {
        if (toAdd.direction === false) {
          this.ridesContainer[pos].forthRide = toAdd;
        } else {
          this.ridesContainer[pos].backRide = toAdd;
        }
      } else {
        let newRide: RidesWithReservationContainer = {
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

    console.log(this.ridesContainer);
  }

  getPositionInContainer(ridesContainer: Array<RidesWithReservationContainer>, date: string): number {
    for (let i = 0; i < ridesContainer.length; i++) {
      if (ridesContainer[i].date === date) {
        return i;
      }
    }

    return -1;
  }

  getReservationInList(res, rideId): ReservationOfRide {
    for (let i = 0; i < res.length; i++) {
      if (res[i].rideId === rideId) {
        return new ReservationOfRide(res[i].id, res[i].stopId, res[i].stopName, res[i].arrivalTime);
      }
    }
    return null;
  }

  onBackClick() {
    this.router.navigate(['reservation']);
  }

  onReservationChange(data) {
    this.getRidesAndReservations();
  }
}
