import { Injectable } from '@angular/core';
import {ActivatedRoute, ActivatedRouteSnapshot, Resolve, Router} from '@angular/router';
import {forkJoin, Observable} from 'rxjs';
import {RideService} from '../services/ride.service';
import {ReservationsService} from '../services/reservations.service';
import {LinesService} from '../services/lines.service';
import {ChildrenService} from '../services/children.service';

@Injectable()
export class ReservationResolverService implements Resolve<Observable<any>> {
  constructor(
    private route: ActivatedRoute, private router: Router,
    private rideService: RideService, private reservationService: ReservationsService,
    private lineService: LinesService, private childrenService: ChildrenService
  ) { }

  resolve(route: ActivatedRouteSnapshot) {
    const childId = route.paramMap.get('child');
    const lineId: number = parseInt(route.paramMap.get('line'), 10);
    const fromDate = route.paramMap.get('from');

    return forkJoin([
      this.lineService
        .getLineNameFromId(
          lineId
        ),
      this.childrenService
        .getChildById(childId),
      this.rideService
        .getNRidesFromDate(
          lineId,
          fromDate,
          7
        ),
      this.reservationService
        .getNReservationsByChildFromDate(
          lineId,
          parseInt(childId, 10),
          fromDate,
          7
        )
    ]);
  }
}
