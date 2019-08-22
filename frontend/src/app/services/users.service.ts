import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, retry} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {Observable, throwError} from 'rxjs';

@Injectable()
export class UsersService {

  constructor(private http: HttpClient) {}

  public getAllusers(): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.usersUrl)
      .pipe(
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
     );
  }

  public getChildForUser(userId): Observable<any> {
    return Observable.create(observer => {
      setTimeout(() => {
        let children = [
          {first_name:"balwant.padwal",last_name:"pune"},
          {first_name:"test",last_name:"mumbai"}]
    
        observer.next(children);
        observer.complete();
      }, 500);
    
    });
  }

}
