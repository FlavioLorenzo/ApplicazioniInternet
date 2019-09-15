import { Injectable } from '@angular/core';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Child } from '../Models/Child';
import {environment} from '../../environments/environment';
import { retry, catchError, tap} from 'rxjs/operators';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ChildrenService {

  private fetchedUsers = [];
  private currentUserChildSubject: BehaviorSubject<Array<Child>>;
  public currentUserChild: Observable<Array<Child>>;


  constructor(private http: HttpClient, private authService: AuthService) {
    this.currentUserChildSubject = new BehaviorSubject<Array<Child>>([]);
    this.currentUserChild = this.currentUserChildSubject.asObservable();

    this.authService.currentUser.subscribe(
      currentUser => {
        if (currentUser) {
            this.updateCurrentUserChildren(currentUser.id);
        }
      }
    );
  }

  private updateCurrentUserChildren(currentUserId) {
    this.getChildrenForUser(currentUserId).subscribe(children => {
      this.fetchedUsers = children;
      this.currentUserChildSubject.next(children);
    });
  }


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
        }),tap(() => {
        //  this.fetchedUsers.push(cpb);
       //   this.currentUserChildSubject.next(this.fetchedUsers);
       this.updateCurrentUserChildren(cpb.userId);

        })
      )

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
        }),tap(() => {
          this.fetchedUsers = this.fetchedUsers.filter(it => childId !== it.childId);
          this.currentUserChildSubject.next(this.fetchedUsers);
        })
      )

  }

}


export class ChildPostBody {
  constructor(public userId: number,
              public firstName: string,
              public lastName: string,
              public phone: string) {}
}
