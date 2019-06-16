import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';

// TODO insert error handler and create model for getReservation

@Injectable()
export class LinesService {

  constructor(private http: HttpClient) {}

  public getAllLines(): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.linesUrl)
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
