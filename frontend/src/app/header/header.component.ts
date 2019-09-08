import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {CurrentUser} from '../Models/currentUser';
import {NotificationsService} from '../services/notifications.service';
import {WebSocketNotificationService} from '../services/web-socket-notification.service';
import {formatNumber} from "@angular/common";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  currentUser: CurrentUser;
  notificationCount = 0;

  constructor(private authenticationService: AuthService,
              private notificationService: NotificationsService,
              private webSocketNotificationService: WebSocketNotificationService) {
  }

  ngOnInit() {
    this.authenticationService.currentUser
      .subscribe(x => {
        this.currentUser = x;
        if (x != null) {
          this.retrieveNotificationCount();

          this.webSocketNotificationService.setupHeader(this);
          this.webSocketNotificationService.connect(this.currentUser.id);
        }
      });
  }

  retrieveNotificationCount() {
    if (this.currentUser != null) {
      this.notificationService.getActiveNotificationsForUser(this.currentUser.id).subscribe(
        (data) => {
          this.notificationCount = data.length;
        },
        (error) => {console.log(error); },
        () => console.log('Done building rides data structure')
      );
    }
  }

  notificationsUpdated() {
    if (this.currentUser != null) {
      this.retrieveNotificationCount();
    }
  }

  onNavBarClick() {
    // If the navbar is not collapsed, collapse it when the user clicks on a link
    if (document.getElementById('ai-navigation-button').offsetParent !== null) {
      document.getElementById('ai-navigation-button').click();
    }
  }

  isCurrentRoleAdmitted(admittedRoles: number[]) {
    const currentRole: number = +this.currentUser.role.id_role;

    for (const role of admittedRoles) {
      if (currentRole === +role) {
        return true;
      }
    }

    return false;
  }
}
