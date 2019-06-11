import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {BehaviorSubject, Observable} from 'rxjs';
import {CurrentUser} from '../Models/currentUser';

@Injectable()
export class AuthService {

  private currentUserSubject: BehaviorSubject<CurrentUser>
  public currentUser: Observable<CurrentUser>

  constructor(private http: HttpClient) {
    this.currentUserSubject = new BehaviorSubject<CurrentUser>
    (JSON.parse(localStorage.getItem('currentUser')));
    this.currentUser = this.currentUserSubject.asObservable();
  }

  public get currentUserValue(): CurrentUser {
    return this.currentUserSubject.value;
  }

  login(email: string, password: string ) {
    return this.http.post<any>('http://localhost:8080/login',
      {email, password})
      .pipe(map(res =>  {
        let cus = null;
        if (res) {
          console.log('setting user...');
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
