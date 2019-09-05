import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Notification} from '../../Models/Notification';
import {Router} from '@angular/router';
import {NotificationsService} from '../../services/notifications.service';
import {MiscUtils} from '../../utils/misc';

@Component({
  selector: 'app-single-notification',
  templateUrl: './single-notification.component.html',
  styleUrls: ['./single-notification.component.css']
})
export class SingleNotificationComponent implements OnInit {
  @Input() notification: Notification;
  @Output() notificationDelete = new EventEmitter();

  constructor(private router: Router,
              private notificationService: NotificationsService) { }

  ngOnInit() {}

  onElementAreaClicked() {
    if (!this.notification.viewed) {
      this.notificationService.viewNotification(this.notification.notificationId).subscribe(
        () => this.notification.viewed = true
      );
    }
  }

  onDeleteClick(event: Event) {
    event.stopPropagation();

    this.notificationService.deleteNotification(this.notification.notificationId).subscribe(
      () => this.notificationDelete.emit(this.notification.notificationId)
    );
  }
}
