import { Injectable } from '@angular/core';
import {Observable, throwError} from 'rxjs';
import {environment} from '../../environments/environment';
import {catchError, retry} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {AuthService} from "./auth.service";
import {Ride} from "../Models/Ride";

@Injectable({
  providedIn: 'root'
})
export class RideService {

  constructor(private http: HttpClient, private auth: AuthService) { }

  public getNRidesFromDate(lineId: number, date: string, n: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/' + lineId +
      '/' + date +
      '/' + n
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public getAvailableStops(rideId: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/' + rideId +
      '/' + 'available'
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public getRideAvailabilityInfo(rideId: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/manage' +
      '/' + this.auth.currentUserValue.id +
      '/' + rideId
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public getAdministeredLinesRidesFromDateToDate(
    userId: number,
    fromDate: string,
    toDate: string,
    openOrLocked: string) {
    return this.http.get<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/manage' +
      '/' + userId +
      '/' + fromDate +
      '/' + toDate +
      '/' + openOrLocked
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  changeLockedStatus(rideId: number, locked: boolean) {
    return this.http.put<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/' + rideId +
      '/lock-unlock',
      {locked}
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
    }

  getLockedRidesFromUserAndDate(userId, date) {
    return this.http.get<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/locked' +
      '/' + userId +
      '/' + date
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  changeRideBookingStatus(openClose: boolean, rideId: number) {
    const action = openClose ? 'close' : 'open';

    return this.http.put<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/' + rideId +
      '/' + action,
      {}
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  closeStop(rideId: number, stopId: number) {
    return this.http.put<any>(
      environment.apiUrl +
      environment.ridesUrl +
      '/' + rideId +
      '/close' +
      '/' + stopId,
      {}
    )
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }
}
