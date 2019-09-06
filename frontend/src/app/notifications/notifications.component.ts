import {Component, OnDestroy, OnInit} from '@angular/core';
import {NotificationsService} from '../services/notifications.service';
import {CurrentUser} from '../Models/currentUser';
import {WebSocketNotificationService} from '../services/web-socket-notification.service';
import {AuthService} from '../services/auth.service';
import {Notification} from '../Models/Notification';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  currentUser: CurrentUser;
  notifications: Array<Notification> = [];

  constructor(private notificationsService: NotificationsService,
              private webSocketNotificationService: WebSocketNotificationService,
              private auth: AuthService) { }

  ngOnInit() {
    this.currentUser = this.auth.currentUserValue;
    this.webSocketNotificationService.setupNotification(this);
    this.webSocketNotificationService.connect(this.currentUser.id);

    this.getNotifications();
  }

  getNotifications() {
    this.notificationsService
      .getNotificationsForUser(this.currentUser.id)
      .subscribe(
        (data) => {
          this.notifications = data;
        },
        (error) => {console.error(error); }
      );
  }

  ngOnDestroy() {
    this.webSocketNotificationService.disconnect();
  }

  onNotificationDeleted() {
    this.getNotifications();
  }

  onNotificationUpdated(event) {
    // Event may be 'new', 'viewed' or 'deleted'
    if ( event === 'new' ) {
      this.getNotifications();
    }
  }
}
