import { Injectable } from '@angular/core';
import {Observable, throwError} from "rxjs";
import {environment} from "../../environments/environment";
import {catchError, retry} from "rxjs/operators";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class StopService {

  constructor(private http: HttpClient) { }

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
}
