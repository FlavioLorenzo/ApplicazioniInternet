import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {AuthService} from './auth.service';

@Injectable()
export class AvailabilityService {
  constructor(private http: HttpClient, private auth: AuthService) {}

  public getAvailabilitiesForRideWithId(rideId: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.availabilitiesUrl +
      '/' + rideId
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public getAvailabilityWithId(availabilityId: number) {
    return this.http.get<any>(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId
    )
      .pipe(
        retry(3),
        catchError( err => {
          console.log(err.message)
          return throwError('Error thrown from catchError');
        })
      );
  }

  public createAvailability(availability: AvailabilityPostBody) {
    availability.userId = this.auth.currentUserValue.id;
    return this.http.post(
      environment.apiUrl +
      environment.availabilityUrl,
      availability
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public modifyAvailability(availabilityId: number, availability: AvailabilityPostBody) {
    availability.userId = this.auth.currentUserValue.id;
    return this.http.put(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId,
      availability
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public deleteAvailability(availabilityId: number) {
    return this.http.delete(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public changeConfirmedStatus(availabilityId: number, status: boolean) {
    return this.http.put(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId +
      environment.confirmedAvailabilityUrl,
      status
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public changeViewedStatus(availabilityId: number, status: boolean) {
    return this.http.put(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId +
      environment.viewedAvailabilityUrl,
      status
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public changeLockedStatus(availabilityId: number, status: boolean) {
    return this.http.put(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId +
      environment.lockedAvailabilityUrl,
      status
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public getNAvailabilitiesByUserFromDate(lineId: number, date: string, n: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.availabilitiesUrl +
      '/' + lineId +
      '/' + this.auth.currentUserValue.id +
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
}

export class AvailabilityPostBody {
  // tslint:disable-next-line:variable-name
  rideId: number;
  // tslint:disable-next-line:variable-name
  userId: number;
  // tslint:disable-next-line:variable-name
  stopId: number;
  constructor(rideId: number,
              userId: number,
              stopId: number) {
    this.rideId = rideId;
    this.stopId = stopId;
    this.userId = userId;
  }
}
