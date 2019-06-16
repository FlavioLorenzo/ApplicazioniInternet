import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {BehaviorSubject, Observable} from 'rxjs';
import {CurrentUser} from '../Models/currentUser';
import {Router} from '@angular/router';

@Injectable()
export class RegistrationService {

  /*
  private currentUserSubject: BehaviorSubject<CurrentUser>;
  public currentUser: Observable<CurrentUser>;
  public redirectUrl: string; // store the URL so we can redirect after logging in
  */

  constructor(private http: HttpClient, private router: Router) {

  }

  register(rpb: RegistrationPostBody) {
    console.log('executing registration'); // for testing only
    // localhost:8080/register è corretto?
    return this.http.post<any>(
      'http://localhost:8080/register',
       rpb)
      .pipe(map(res =>  {
        if (res) {
          this.router.navigate(['/login']);
        }
     }));
  }
}

export class RegistrationPostBody {
  email: string;
  password: string;
  // tslint:disable-next-line:variable-name
  confirm_password: string;
  // tslint:disable-next-line:variable-name
  first_name: string;
  // tslint:disable-next-line:variable-name
  last_name: string;
  constructor(email: string,
              password: string,
              confirmPassword: string,
              firstName: string,
              lastName: string) {
    this.email = email;
    this.password = password;
    this.confirm_password = confirmPassword;
    this.first_name = firstName;
    this.last_name = lastName;
  }
}
