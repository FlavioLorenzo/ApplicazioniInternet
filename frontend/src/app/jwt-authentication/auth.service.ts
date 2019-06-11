import { Injectable } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { catchError, map } from 'rxjs/operators';
import {Observable} from "rxjs";

@Injectable()
export class AuthService {

  public isAuthenticated(): boolean {
    return localStorage.getItem('id_token') !== null;
  }

  constructor(private http: HttpClient) {
  }

  login(email: string, password: string ) {
    return this.http.post<any>('http://localhost:8080/login', {email, password})
      .pipe(map(res =>  {
        if (res) {
          this.setSession(res);
        }
      }),
        catchError(e => {
          return null;
        })
      );
  }

  private setSession(authResp) {
    localStorage.setItem('id_token', authResp.token);
  }

  public getToken() {
    return localStorage.getItem('id_token');
  }

  public logout() {
    localStorage.removeItem('id_token');
  }
}
