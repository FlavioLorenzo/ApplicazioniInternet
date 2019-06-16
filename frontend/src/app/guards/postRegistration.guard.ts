import { Injectable } from '@angular/core';
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { PostRegistrationService } from '../services/PostRegistration.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(public postRegistrationService: PostRegistrationService,
              public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.checkLogin(state.url);
  }
  checkValidDataRegistration()
  {
    this.postRegistrationService.getConfirmati();
  }

  checkLogin(url: string): boolean {
    if (this.authenticationService.currentUserValue) { return true; }

    // Store the attempted URL for redirecting
    this.authenticationService.redirectUrl = url;

    // Navigate to the login page with extras
    this.router.navigate(['/login']);
    return false;
  }

}
