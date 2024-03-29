import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {ReservationsService} from '../../services/reservations.service';
import {LinesService} from '../../services/lines.service';
import {forkJoin} from 'rxjs';
import {ReservationOfRide, RidesWithReservationContainer} from '../rides-with-reservation-container';
import {RideService} from '../../services/ride.service';
import {ChildrenService} from '../../services/children.service';
import {Child} from '../../Models/Child';

@Component({
  selector: 'app-reservation-ride-display',
  templateUrl: './reservation-ride-display.component.html',
  styleUrls: ['./reservation-ride-display.component.css']
})
export class ReservationRideDisplayComponent implements OnInit {
  childId: number;
  lineId: number;
  lineName: string;
  fromDate: string;
  child: Child;
  rides: any;
  ridesContainer: Array<RidesWithReservationContainer> = [];

  constructor(private route: ActivatedRoute, private router: Router,
              private rideService: RideService, private reservationService: ReservationsService,
              private lineService: LinesService, private childrenService: ChildrenService) { }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.childId = params.child;
      this.lineId = params.line;
      this.fromDate = params.from;
    });

    this.lineName = this.route.snapshot.data.info[0].name;
    this.child = this.route.snapshot.data.info[1];
    this.buildRidesContainer(this.route.snapshot.data.info[2], this.route.snapshot.data.info[3]);
  }

  getRidesAndReservations() {
    forkJoin([
      this.lineService
        .getLineNameFromId(
          this.lineId
        ),
      this.childrenService
        .getChildById(this.childId.toString()),
      this.rideService
        .getNRidesFromDate(
          this.lineId,
          this.fromDate,
          7
        ),
      this.reservationService
        .getNReservationsByChildFromDate(
          this.lineId,
          this.childId,
          this.fromDate,
          7
        )
    ]).subscribe(
      (data) => {
        this.lineName = data[0].name;
        this.child = data[1];
        this.buildRidesContainer(data[2], data[3]);
      },
      (error) => {console.log(error); },
      () => console.log('Done building data structure')
    );
  }

  buildRidesContainer(rides, res) {
    for (let i = 0, len = rides.length; i < len; i++) {
      const pos = this.getPositionInContainer(this.ridesContainer, rides[i].date);
      const reservation = this.getReservationInList(res, rides[i].id);
      const toAdd = rides[i];

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
        const newRide: RidesWithReservationContainer = {
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
