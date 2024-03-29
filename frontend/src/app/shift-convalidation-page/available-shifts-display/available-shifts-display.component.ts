import { Component, OnInit } from '@angular/core';
import {DateUtilsService} from '../../services/date-utils.service';
import {AuthService} from '../../services/auth.service';
import {RideService} from '../../services/ride.service';
import {RidesSummaryByDateContainer, RideSummary} from '../rides-summary-by-date-container';
import {MatDialog, MatSnackBar} from '@angular/material';
import {FilterAvailabilitiesPopupComponent} from './filter-availabilities-popup/filter-availabilities-popup.component';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-available-shifts-display',
  templateUrl: './available-shifts-display.component.html',
  styleUrls: ['./available-shifts-display.component.css']
})
export class AvailableShiftsDisplayComponent implements OnInit {
  userId: number;
  fromDate: string;
  toDate: string;
  openOrLocked: string;
  ridesByDay: Array<RidesSummaryByDateContainer> = [];

  constructor(private route: ActivatedRoute, private dateService: DateUtilsService, private auth: AuthService,
              private rideService: RideService, private dialog: MatDialog, private snackBar: MatSnackBar) { }

  ngOnInit() {
    this.userId = this.auth.currentUserValue.id;
    this.fromDate = this.dateService.getCurrentDate();
    this.toDate = this.dateService.getDateAfterNDays(7);
    this.openOrLocked = 'open';

    this.ridesByDay = this.route.snapshot.data.rides;
  }

  getRidesAndAvailabilities() {
    this.rideService
      .getAdministeredLinesRidesFromDateToDate(
        this.userId,
        this.fromDate,
        this.toDate,
        this.openOrLocked
      ).subscribe(
        (data) => { this.ridesByDay = data; },
        (error) => {console.log(error); },
        () => console.log('Done building data structure'));
  }

  onShiftChange(ride: RideSummary) {
    this.rideService.changeLockedStatus(ride.id, !ride.locked).subscribe(
      () => { this.getRidesAndAvailabilities(); },
      (error) => {
        console.log(error);
        this.snackBar.open('Cannot change the status of the ride.');
      },
      () => console.log('Done building data structure'));
  }

  onFilterButtonClick() {
    const dialogRef = this.dialog.open(FilterAvailabilitiesPopupComponent, {
      width: '250px',
      data: {
        rideToShow: this.openOrLocked,
        fromDate: this.fromDate,
        toDate: this.toDate
      }
    });

    dialogRef.afterClosed().subscribe(() => {
      console.log('Closed dialog...');
    });

    dialogRef.componentInstance.update.subscribe(data => {
      if (data.rideToShow != null) {
        this.openOrLocked = data.rideToShow;
      }

      if (data.fromDate != null) {
        this.fromDate = data.fromDate;
      }

      if (data.toDate != null ) {
        this.toDate = data.toDate;
      }

      this.getRidesAndAvailabilities();
    });
  }
}
