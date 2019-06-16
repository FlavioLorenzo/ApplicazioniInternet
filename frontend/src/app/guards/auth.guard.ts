import { Injectable } from '@angular/core';
import {
  Router,
  CanActivate,
  ActivatedRouteSnapshot,
  RouterStateSnapshot
} from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(public authenticationService: AuthService,
              public router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {

    return this.checkLogin(state.url);
  }

  checkLogin(url: string): boolean {
    if (this.authenticationService.currentUserValue) { return true; }

    // Store the attempted URL for redirecting
    this.authenticationService.redirectUrl = url;

    // Navigate to the login page with extras
  //  this.router.navigate(['/login']);
    return false;
  }

}
