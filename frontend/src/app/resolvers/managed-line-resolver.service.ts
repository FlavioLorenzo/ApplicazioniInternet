import { Injectable } from '@angular/core';
import {Resolve} from '@angular/router';
import {Observable} from 'rxjs';
import {RideService} from '../services/ride.service';
import {AuthService} from '../services/auth.service';
import {DateUtilsService} from '../services/date-utils.service';
import { Line } from '../Models/Line';
import { RegistrationService } from '../services/registration.service';

@Injectable()
export class ManagedLineResolverService implements Resolve<Observable<Line[]>> {
  constructor(
    private reg: RegistrationService,
    private auth: AuthService,
      ) { }

  resolve() {
    console.log(`Resolving managed lines...`);
    return this.reg.getAdministeredLineOfUser(
      this.auth.currentUserValue.id);
  }
}
