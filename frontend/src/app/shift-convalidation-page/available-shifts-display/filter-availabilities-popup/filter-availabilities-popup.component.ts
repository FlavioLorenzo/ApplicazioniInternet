import {Component, EventEmitter, Inject, OnInit, Output} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {DateUtilsService} from "../../../services/date-utils.service";

@Component({
  selector: 'app-filter-availabilities-popup',
  templateUrl: './filter-availabilities-popup.component.html',
  styleUrls: ['./filter-availabilities-popup.component.css']
})
export class FilterAvailabilitiesPopupComponent implements OnInit {
  @Output()
  update = new EventEmitter();

  rideToShow: string;
  fromDate: string;
  toDate: string;

  constructor(
    public dialogRef: MatDialogRef<FilterAvailabilitiesPopupComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {rideToShow, fromDate, toDate},
    private dateService: DateUtilsService) {
    this.rideToShow = data.rideToShow;
    this.fromDate = data.fromDate;
    this.toDate = data.toDate;
  }

  ngOnInit() {
  }

  onUndoClick(): void {
    this.dialogRef.close();
  }

  onApplyClick() {
    this.update.emit({
        rideToShow: this.rideToShow,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
      );
    this.dialogRef.close();
  }

  onFromDateChanged(date: string) {
    this.fromDate = date;
  }

  onToDateChanged(date: string) {
    this.toDate = date;
  }
}
