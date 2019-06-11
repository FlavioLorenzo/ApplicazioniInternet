import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from 'src/app/jwt-authentication/auth.service';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  constructor(private authenticationService: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // add authorization header with jwt token if available
    const idToken = this.authenticationService.getToken();
    if (idToken) {
      console.log('jwt intercepted ' + idToken);
      const cloned = request.clone({
        setHeaders: {
          Authorization: `Bearer ${idToken}`
        }
      });
      return next.handle(cloned);
    } else {
      return next.handle(request);
    }
  }
}
