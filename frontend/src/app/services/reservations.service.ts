import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';

// TODO insert error handler and create model for getReservation

@Injectable()
export class ReservationsService {

  constructor(private http: HttpClient) {}

  public getReservationsForLineAndDay(
    lineId: number, date: string): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.reservationsUrl +
      '/' + lineId + '/' + date)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
     );
  }

  public getReservation(
    lineId: number, date: string, reservationId: number) {
    return this.http.get<any>(
      environment.apiUrl +
      environment.reservationsUrl +
      '/' + lineId + '/' + date + '/' + reservationId)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public modifyReservation(
    lineId: number, date: string, reservationId: number,
    rpb: ReservationPostBody) {
    return this.http.put(
      environment.apiUrl +
      environment.reservationsUrl +
      '/' + lineId + '/' + date + '/' + reservationId,
      rpb)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public deleteReservation(
    lineId: number, date: string, reservationId: number) {
    return this.http.delete(
      environment.apiUrl +
      environment.reservationsUrl + '/' + lineId + '/' + date + '/' + reservationId)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public createReservation(
    lineId: number, date: string, reservationId: number,
    rpb: ReservationPostBody) {
    return this.http.put(
  environment.apiUrl +
      environment.reservationsUrl +
      '/' + lineId + '/' + date,
      rpb)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }
}


export class ReservationPostBody {
  userId: number;
  joinStop: number;
  leaveStop: number;
  direction: boolean;
}
