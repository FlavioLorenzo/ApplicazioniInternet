import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {AuthService} from './auth.service';
import {Observable, throwError} from 'rxjs';
import {catchError, retry} from 'rxjs/operators';
import {AvailabilityPostBody} from './availability.service';

@Injectable()
export class NotificationsService {
  webSocketEndPoint = environment.apiUrl + environment.socketUrl;
  stompClient: any;
  userId: number;

  constructor(private http: HttpClient, private auth: AuthService) {}

  public getActiveNotificationsForUser(userId: number): Observable<any> {
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

  public getNotificationWithId(notificationId: number) {
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

  public createNotification(notification: NotificationPostBody) {
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

  public modifyAvailability(availabilityId: number, availability: AvailabilityPostBody) {
    availability.userId = this.auth.currentUserValue.id;
    return this.http.put(
      environment.apiUrl +
      environment.availabilityUrl +
      '/' + availabilityId,
      availability
    )
      .pipe(
        retry(3),
        catchError(err => {
          console.log(err.message);
          return throwError('Error thrown from catchError');
        })
      );
  }

  public deleteAvailability(notificationId: number) {
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

  public viewNotification(notificationId: number) {
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


  connect(userId: number) {
    this.userId = userId;
    const ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);

    this.stompClient.connect({}, function(frame) {
      this.stompClient.subscribe('/messages/' + userId, () => {
        // TODO execute update on the component displaying notifications
      });
    }, this.errorCallBack);
  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }

  errorCallBack(error) {
    setTimeout(() => {
      this.connect(this.userId);
    }, 5000);
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
