import {Component, OnDestroy, OnInit} from '@angular/core';
import {NotificationsService} from '../services/notifications.service';
import {CurrentUser} from '../Models/currentUser';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.css']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  currentUser: CurrentUser;
  notifications = [];

  constructor(private notificationsService: NotificationsService) { }

  ngOnInit() {
    this.notificationsService.setup(this);
    this.notificationsService
      .getActiveNotificationsForUser(this.currentUser.id)
      .subscribe(
        (data) => {
          this.notifications = data;
         },
        (error) => {console.error(error); },
        () => {console.log('Lines retrieved'); }
      );
    this.notificationsService.connect(this.currentUser.id);
  }

  ngOnDestroy() {
    this.notificationsService.disconnect();
  }
}
