import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Child } from '../Models/Child';
import {environment} from '../../environments/environment';
import { retry, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ChildrenService {

  constructor(private http: HttpClient) { }

  public getAllChildren() {
    return this.http.get<any>(
      `${environment.apiUrl}/children`)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public getChildrenForUser(userId: number): Observable<any> {

    return this.http.get<any>(
      `${environment.apiUrl}/children/${userId}`)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );

  }

  public getAllFreeChildren(date: string, direction: boolean): Observable<any> {

    return this.http.get<any>(
      `${environment.apiUrl}/children/free/${date}/${direction}`)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );

  }

  public getChildById(childId: string): Observable<any> {

    return this.http.get<any>(
      `${environment.apiUrl}/child/${childId}`)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );

  }

  public registerChild(cpb: ChildPostBody): Observable<any> {

    return this.http.post<any>(
      `${environment.apiUrl}/child/`,
      cpb)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );

  }

  public deleteChild(childId: number): Observable<any> {

    return this.http.delete<any>(
      `${environment.apiUrl}/child/${childId}`)
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


export class ChildPostBody {
  constructor(public childId: number,
              public firstName: string,
              public lastName: string,
              public phone: string) {}
}
