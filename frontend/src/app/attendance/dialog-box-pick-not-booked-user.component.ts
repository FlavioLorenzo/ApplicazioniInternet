import {Component, EventEmitter, Inject, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Ride} from '../Models/Ride';
import {User} from '../Models/User';
import {BusStop} from '../Models/BusLineStop';

@Component({
  selector: 'app-dialog-box-pick-not-booked-user',
  templateUrl: 'dialog-box-pick-not-booked-user.html',
})
export class DialogBoxPickNotBookedUserComponent {
  selectedStop: BusStop;
  @Output()
  update = new EventEmitter();

  constructor(
    public dialogRef: MatDialogRef<DialogBoxPickNotBookedUserComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {ride: Ride, user: any, direction: number}) {
    this.selectedStop = data.ride.stopList[0];
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(selectedStop): void {
    this.update.emit(selectedStop);
    this.dialogRef.close();
  }
}
