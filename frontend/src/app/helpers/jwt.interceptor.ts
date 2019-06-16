import { Injectable } from '@angular/core';
import {HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/services/auth.service';
import {tap} from 'rxjs/operators';
import {Router} from '@angular/router';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(
    private authenticationService: AuthService,
    private router: Router) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    const currentUser = this.authenticationService.currentUserValue;
    if (currentUser && currentUser.token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
    }

    return next.handle(request).pipe( tap(() => {},
      (err: any) => {
      if (err instanceof HttpErrorResponse) {
          if (err.status !== 401) {
            return;
          }
          this.authenticationService.logout();
          this.router.navigate(['/login']);
        }
      }));
    }
}
