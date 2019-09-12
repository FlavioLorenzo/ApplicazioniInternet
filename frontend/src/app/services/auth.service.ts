import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import {catchError, map, retry} from 'rxjs/operators';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {CurrentUser} from '../Models/currentUser';
import {Router} from '@angular/router';
import {environment} from '../../environments/environment';
import {User} from '../Models/User';

@Injectable()
export class AuthService {

  private currentUserSubject: BehaviorSubject<CurrentUser>;
  public currentUser: Observable<CurrentUser>;
  public redirectUrl: string; // store the URL so we can redirect after logging in

  constructor(private http: HttpClient, private router: Router) {
    this.currentUserSubject = new BehaviorSubject<CurrentUser>
    (JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): CurrentUser {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string ) {
    return this.http.post<any>(environment.apiUrl + '/login',
      {email, password})
      .pipe(map(res =>  {
        let cus = null;
        if (res) {

          // Create and set the new user
          cus = new CurrentUser(res.id, res.mail, res.token, res.role);
          localStorage.setItem('currentUser', JSON.stringify(cus));
          this.currentUserSubject.next(cus);

          // Handle the redirect logic after the login
          this.handleRedirectUrl();

        }
        return cus;
      }));
  }

  public logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }

  updateUserRole() {
    return this.http.get<any>(environment.apiUrl + '/updated-user-role')
      .pipe(map(res =>  {
        let cus = null;

        console.log(res);
        console.log(this.currentUserSubject.value.role);
        if (res && (res.role.id_role !== this.currentUserSubject.value.role.id_role)) {
          const previous = this.currentUserSubject.value;

          // Create and set the new user
          cus = new CurrentUser(previous.id, previous.mail, previous.token, res.role);
          localStorage.setItem('currentUser', JSON.stringify(cus));
          this.currentUserSubject.next(cus);

          // Handle the redirect logic after the login
          this.handleRedirectUrl();

        }
        return cus;
      }));
  }

  handleRedirectUrl() {
    if (this.redirectUrl) {
      console.log('Redirect url is:', this.redirectUrl);
      this.router.navigate([this.redirectUrl]);
      this.redirectUrl = null;
    } else {
      console.log('Redirecting to home');
      this.router.navigate(['/']);
    }
  }

  /* Should pop up a message saying restore link has been sent */
  public recoverPassword(email: string) {
    return this.http.post<any>(environment.apiUrl + '/recover',
      {email})
      .pipe(
        retry(1),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
      );
  }

  public resetPassword(
    recoverToken: string,
    password: string,
    confirmPassword: string
  ) {
    return this.http.post<string>(
      environment.apiUrl + '/recover/' + recoverToken,
      {password, confirmPassword})
      .pipe(retry(1),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        }),
        map(res => {
          // TODO: action to perform after registration
        }));
  }

  public getResetTokenInfo(token: string) {
    return this.http.get<any>(environment.apiUrl + '/recover/' + token)
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
