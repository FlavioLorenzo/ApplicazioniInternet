import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {BusStop} from '../../../Models/BusLineStop';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {Ride} from '../../../Models/Ride';
import {RideWithReservation} from "../../rides-container";

@Component({
  selector: 'app-modify-stop-popup',
  templateUrl: './modify-stop-popup.component.html',
  styleUrls: ['./modify-stop-popup.component.css']
})
export class ModifyStopPopupComponent implements OnInit {
  ride: RideWithReservation;
  selectedStop: number;
  @Output()
  update = new EventEmitter();
  constructor(
    public dialogRef: MatDialogRef<ModifyStopPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {ride}) {
    this.ride = data.ride;
    this.selectedStop = this.ride.reservation.stopId;
  }
  ngOnInit() {
  }

  onUndoClick(): void {
    this.dialogRef.close();
  }

  onModifyClick(selectedStop): void {
    this.update.emit(selectedStop);
    this.dialogRef.close();
  }

  onStopSelected(value: number) {
    this.selectedStop = value;
  }
}
