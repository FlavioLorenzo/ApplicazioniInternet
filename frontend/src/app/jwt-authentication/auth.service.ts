import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {BehaviorSubject, Observable} from 'rxjs';
import {CurrentUser} from '../Models/currentUser';
import {Router} from '@angular/router';

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

    console.log('Prelogin redirect url is:', this.redirectUrl);

    return this.http.post<any>('http://localhost:8080/login',
      {email, password})
      .pipe(map(res =>  {
        let cus = null;
        if (res) {

          console.log('Redirect url is:', this.redirectUrl);

          if (this.redirectUrl) {
            this.router.navigate([this.redirectUrl]);
            this.redirectUrl = null;
          }

          cus = new CurrentUser(res.mail, res.token)
          localStorage.setItem('currentUser', JSON.stringify(cus));
          this.currentUserSubject.next(cus);
        }
        return cus;
      }));
  }

  public logout() {
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
  }
}