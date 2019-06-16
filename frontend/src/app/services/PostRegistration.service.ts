import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {catchError, map, retry} from 'rxjs/operators';
import {Router} from '@angular/router';
// tslint:disable-next-line:import-spacing
import {environment}
from '../../environments/environment';
import {Observable, throwError} from 'rxjs';

@Injectable()
export class PostRegistrationService {
  public redirectUrl: string; // store the URL so we can redirect after logging in

  constructor(private http: HttpClient, private router: Router) {
    console.log('NEW ISTANCE OF MAIL SERVICE');
  }

  // i've imposted the request to /register but i don't know if it is the correct link
  public getConfirmation(): Observable<any> {

    return this.http.get<any>(
      environment.apiUrl +
      environment.register)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}
