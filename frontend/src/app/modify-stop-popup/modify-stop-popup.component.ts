import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';

@Component({
  selector: 'app-modify-stop-popup',
  templateUrl: './modify-stop-popup.component.html',
  styleUrls: ['./modify-stop-popup.component.css']
})
export class ModifyStopPopupComponent implements OnInit {
  rideId: number;
  selectedStop: number;
  @Output()
  update = new EventEmitter();
  constructor(
    public dialogRef: MatDialogRef<ModifyStopPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {rideId, selectedStop}) {
    this.rideId = data.rideId;
    this.selectedStop = data.selectedStop;
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
