import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import {BehaviorSubject, Observable} from 'rxjs';
import {CurrentUser} from '../Models/currentUser';
import {Router} from '@angular/router';

@Injectable()
export class HttpRegistrationService {

  /*
  private currentUserSubject: BehaviorSubject<CurrentUser>;
  public currentUser: Observable<CurrentUser>;
  public redirectUrl: string; // store the URL so we can redirect after logging in
  */

  constructor(private http: HttpClient, private router: Router) {

  }

  Register(email: string, password: string, confirmPassword: string, firstName: string, lastName: string) {
    console.log('executing registration'); // for testing only
    // localhost:8080/register è corretto?
    return this.http.post<any>('http://localhost:8080/register',
      {email, password, confirmPassword, firstName, lastName})
      .pipe(map(res =>  {
        if (res) {
          this.router.navigate(['/login']);
        }
     }));
  }
}
