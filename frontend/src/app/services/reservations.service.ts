import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';
import {Ride} from '../Models/Ride';
import {environment} from '../../environments/environment';

// TODO insert error handler and create model for getReservation

@Injectable()
export class ReservationsService {

  constructor(private http: HttpClient) {}

  public getReservationsForLineAndDay(
    lineId: number, date: string) {
    return this.http.get<Ride[]>(
      environment.apiUrl + '/' + lineId + '/' + date)
      .pipe(
        retry(3),
        // catchError()
      );
  }

  public getReservation(
    lineId: number, date: string, reservationId: number) {
    return this.http.get<any>(
      environment.apiUrl + '/' + lineId + '/' + date + '/' + reservationId)
      .pipe(
        retry(3),
        // catchError()
      );
  }

  public modifyReservation(
    lineId: number, date: string, reservationId: number,
    rpb: ReservationPostBody) {
    return this.http.put(
      environment.apiUrl + '/' + lineId + '/' + date + '/' + reservationId,
      rpb)
      .pipe(
        retry(3),
        // catchError()
      );
  }

  public deleteReservation(
    lineId: number, date: string, reservationId: number) {
    return this.http.delete(
      environment.apiUrl + '/' + lineId + '/' + date + '/' + reservationId)
      .pipe(
        retry(3),
        // catchError()
      );
  }

  public createReservation(
    lineId: number, date: string, reservationId: number,
    rpb: ReservationPostBody) {
    return this.http.put(
  environment.apiUrl + '/' + lineId + '/' + date,
      rpb)
      .pipe(
        retry(3),
        // catchError()
      );
  }
}


export class ReservationPostBody {
  userId: number;
  joinStop: number;
  leaveStop: number;
  direction: boolean;
}
