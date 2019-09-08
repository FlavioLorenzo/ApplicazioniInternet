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
    if (!this.checkLogin(state.url)) {
      return false;
    }

    if (!this.checkCredentials(state.url)) {
      this.router.navigate(['/reservation']); // TODO: Must be changed to home component
      return false;
    }

    return true;
  }

  checkLogin(url: string): boolean {
    if (this.authenticationService.currentUserValue) { return true; }

    // Store the attempted URL for redirecting
    // this.authenticationService.redirectUrl = url;

    // Navigate to the login page with extras
    this.router.navigate(['/login']);
    return false;
  }

  checkCredentials(url: string): boolean {
    switch (+this.authenticationService.currentUserValue.role.id_role) {
      case 1:
      case 2:
        return true;
      case 3:
        return !(url.search('/shift-consolidation') >= 0);
      default:
        return !(
          (url.search('/shift-consolidation')) >= 0 ||
          (url.search('/shift-definition')) >= 0 ||
          (url.search('/attendance') >= 0) ||
          (url.search('/users') >= 0)
        );
    }
  }
}
