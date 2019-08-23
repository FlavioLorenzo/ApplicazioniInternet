import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {environment} from '../../environments/environment';

@Injectable()
export class AvailabilityService {
  constructor(private http: HttpClient) {}

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

  public createAvailability(availability: AvailabilityBody) {
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

  public modifyAvailability(availabilityId: number, availability: AvailabilityBody) {
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
}

export class AvailabilityBody {
  // tslint:disable-next-line:variable-name
  ride_id: number;
  // tslint:disable-next-line:variable-name
  user_id: number;
  // tslint:disable-next-line:variable-name
  stop_id: number;
}
