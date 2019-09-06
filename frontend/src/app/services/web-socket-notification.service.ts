import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import {NotificationsComponent} from '../notifications/notifications.component';
import {HeaderComponent} from "../header/header.component";

@Injectable()
export class WebSocketNotificationService {
  webSocketEndPoint = environment.apiUrl + environment.socketUrl;
  stompClient: any;
  notificationsComponent: NotificationsComponent;
  headerComponent: HeaderComponent;
  userId: number;

  constructor() { }

  setupNotification(notificationsComponent: NotificationsComponent) {
    this.notificationsComponent = notificationsComponent;
  }

  setupHeader(headerComponent: HeaderComponent) {
    this.headerComponent = headerComponent;
  }

  connect(userId: number) {
    this.userId = userId;

    const ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const that = this;

    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/messages/' + that.userId, (data) => {
        // Data contains the type of event that took place. Possible values are 'new', 'viewed' or 'deleted'

        if (that.notificationsComponent != null) {
          that.notificationsComponent.onNotificationUpdated(data.body);
        }
        if (that.headerComponent != null ) {
          that.headerComponent.notificationsUpdated();
        }
      });
    }, this.errorCallBack);
  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
  }

  errorCallBack() {
    setTimeout(() => {
      this.connect(this.userId);
    }, 5000);
  }

  notifyUser(userId: number) {
    this.stompClient.send(
      '/app' + environment.notificationUpdateUrl + '/' + userId, {}, {});
  }
}
