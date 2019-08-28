import { Injectable } from '@angular/core';
import {HttpClientModule, HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, map, retry} from 'rxjs/operators';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
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
    return this.http.post<any>(
      'http://localhost:8080/register',
       rpb)
      .pipe(map(res =>  {
        if (res) {
          this.router.navigate(['/login']);
        }
     }));
  }

  checkEmail(email: string) {
    const requestOptions: object = {
      responseType: 'text'
      };

    return this.http.post<any>(
      'http://localhost:8080/check-email',
      {email},
      requestOptions)
      .pipe(retry(3));
  }

  checkCode(code: string) {
    //TODO: TO IMPLEMENT
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
