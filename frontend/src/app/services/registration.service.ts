import { Injectable } from '@angular/core';
import {HttpClientModule, HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, retry} from 'rxjs/operators';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {CurrentUser} from '../Models/currentUser';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';

@Injectable()
export class RegistrationService {

  constructor(private http: HttpClient, private router: Router) {}

  register(rpb: RegistrationPostBody) {
    return this.http.post<any>(
       `${environment.apiUrl}/register`,
       rpb)
      .pipe(map(res =>  {
        // TODO: Action to perform
     }));
  }

  completeRegistration(token: string, rpb: CompleteUserPostBody) {
    return this.http.post<any>(
       `${environment.apiUrl}/complete-registration/${token}`,
       rpb)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        }),
        map(res => {
          // TODO: action to perform after registration
        })
      );
  }

  checkEmail(email: string) {
    const requestOptions: object = { responseType: 'text' }; //Waiting for  an "ok" from the server

    return this.http.post<any>(
      `${environment.apiUrl}/check-email`,
      {email},
      requestOptions)
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  confirmRegistrationToken(token: string) {
    return this.http.post<any>(
      `${environment.apiUrl}/token-info/${token}`,
       {})
       .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  addAdminRoleOfLineToUser(userId: number, lineId: number){

    return this.http.post<any>(
      `${environment.apiUrl}/user/add-admin/${userId}/${lineId}`,
       {})
       .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );

  }

  removeAdminRoleOfLineFromUser(userId: number, lineId: number){

    return this.http.post<any>(
      `${environment.apiUrl}/user/remove-admin/${userId}/${lineId}`,
       {})
       .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );

  }

  getAdministeredLineOfUser(userId: number){
    return this.http.post<any>(
      `${environment.apiUrl}/user/administered-line/${userId}`,
       {})
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

export class RegistrationPostBody {
  constructor(public email: string,
              public first_name: string,
              public last_name: string,
              public role: number) {}
}

export class CompleteUserPostBody {
  constructor(public password: string,
              public confirm_password: string,
              public phone: string) {}
}

export class RecoverNewPwdPostBody{
    constructor(public password: string,
                public confirm_password: string) {}
}
