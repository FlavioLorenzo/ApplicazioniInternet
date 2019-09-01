import { Injectable } from '@angular/core';
import {Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {RideService} from '../services/ride.service';
import {AuthService} from '../services/auth.service';
import {DateUtilsService} from '../services/date-utils.service';

@Injectable()
export class AttendanceResolverService implements Resolve<Observable<any>> {
  constructor(
    private rideService: RideService,
    private auth: AuthService,
    private dateService: DateUtilsService
  ) { }

  resolve() {
    return this.rideService.getLockedRidesFromUserAndDate(
      this.auth.currentUserValue.id,
      this.dateService.getCurrentDate());
  }
}
