import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {DateUtilsService} from '../../services/date-utils.service';
import {RideService} from '../../services/ride.service';
import {AuthService} from '../../services/auth.service';
import {Ride} from '../../Models/Ride';

@Component({
  selector: 'app-attendance-display',
  templateUrl: './attendance-display.component.html',
  styleUrls: ['./attendance-display.component.css']
})
export class AttendanceDisplayComponent implements OnInit {
  date: string;
  rides: Array<Ride> = [];
  curPage = 0;

  constructor(private route: ActivatedRoute, private router: Router, private dateService: DateUtilsService,
              private rideService: RideService, private auth: AuthService) { }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.date = params.from;
    });

    if (this.date == null) {
      this.date = this.dateService.getCurrentDate();
      this.getRides();
    }
  }

  getRides() {
    this.rideService.getLockedRidesFromUserAndDate(this.auth.currentUserValue.id, this.date).subscribe(
      (data) => {
        console.log(data);
        this.rides = data;
      },
      (error) => {console.log(error); },
      () => console.log('Done building rides data structure')
    );
  }

  changePage(page) {
    this.curPage = page.pageIndex;
  }

  onBackClick() {
    this.router.navigate(['attendance', 'selection']);
  }

  onAttendanceChanged() {
    this.getRides();
  }
}
