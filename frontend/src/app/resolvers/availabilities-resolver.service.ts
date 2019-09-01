import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {forkJoin, Observable} from 'rxjs';
import {RideService} from '../services/ride.service';
import {LinesService} from '../services/lines.service';
import {AvailabilityService} from '../services/availability.service';

@Injectable()
export class AvailabilitiesResolverService implements Resolve<Observable<any>> {
  constructor(
    private lineService: LinesService, private rideService: RideService,
    private availabilityService: AvailabilityService
  ) { }

  resolve(route: ActivatedRouteSnapshot) {
    const lineId = parseInt(route.paramMap.get('id'), 10);
    const fromDate = route.paramMap.get('from');

    return forkJoin([
      this.lineService
        .getLineNameFromId(
          lineId
        ),
      this.rideService
        .getNRidesFromDate(
          lineId,
          fromDate,
          7
        ),
      this.availabilityService
        .getNAvailabilitiesByUserFromDate(
          lineId,
          fromDate,
          7
        )
    ]);
  }
}
