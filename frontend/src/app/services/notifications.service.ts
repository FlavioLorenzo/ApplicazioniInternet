import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {AuthService} from './auth.service';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {NotificationsComponent} from '../notifications/notifications.component';

@Injectable()
export class NotificationsService {
  constructor(
    private http: HttpClient,
    private auth: AuthService,
  ) {}

  getNotificationsForUser(userId: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.allNotificationsUrl +
      '/' + userId
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  getActiveNotificationsForUser(userId: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.pendingNotificationsUrl +
      '/' + userId
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  getNotificationWithId(notificationId: number): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.notificationsUrl +
      '/' + notificationId
    )
      .pipe(
        retry(3),
        catchError( err => {
          console.log(err.message)
          return throwError('Error thrown from catchError');
        })
      );
  }

  createNotification(notification: NotificationPostBody): Observable<any> {
    return this.http.post(
      environment.apiUrl +
      environment.notificationsUrl,
      notification
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }


  deleteNotification(notificationId: number) {
    return this.http.delete(
      environment.apiUrl +
      environment.notificationsUrl +
      '/' + notificationId
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  viewNotification(notificationId: number): Observable<any> {
    return this.http.put(
      environment.apiUrl +
      environment.viewedNotificationUrl +
      '/' + notificationId,
      {}
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }
}

export class NotificationPostBody {
  // tslint:disable-next-line:variable-name
  user_id: number;
  message: string;
  link: string;
  constructor(
    // tslint:disable-next-line:variable-name
    user_id: number,
    message: string,
    link: string
  ) {
    this.user_id = user_id;
    this.message = message;
    this.link = link;
  }
}
