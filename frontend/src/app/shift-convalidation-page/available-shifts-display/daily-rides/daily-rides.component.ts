import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Router} from '@angular/router';
import {RidesSummaryByDateContainer, RideSummary} from '../../rides-summary-by-date-container';

@Component({
  selector: 'app-daily-rides',
  templateUrl: './daily-rides.component.html',
  styleUrls: ['./daily-rides.component.css']
})
export class DailyRidesComponent implements OnInit {
  @Input() dailyRides: RidesSummaryByDateContainer;
  @Output() shiftChange = new EventEmitter<RideSummary>();

  constructor(private router: Router) { }

  ngOnInit() {
  }

  onElementAreaClicked(ride: RideSummary) {
    this.router.navigate(['shift-consolidation', ride.id]);
  }

  onCloseOpenButtonClick(event: Event, ride: RideSummary) {
    event.stopPropagation();
    this.shiftChange.emit(ride);
  }
}
