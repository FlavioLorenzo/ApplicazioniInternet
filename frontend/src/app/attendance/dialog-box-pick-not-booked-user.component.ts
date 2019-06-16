import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Ride} from '../Models/Ride';
import {User} from '../Models/User';

@Component({
  selector: 'app-dialog-box-pick-not-booked-user',
  templateUrl: 'dialog-box-pick-not-booked-user.html',
})
export class DialogBoxPickNotBookedUserComponent {

  constructor(
    public dialogRef: MatDialogRef<DialogBoxPickNotBookedUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {ride: Ride, user: User}) {
  }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
