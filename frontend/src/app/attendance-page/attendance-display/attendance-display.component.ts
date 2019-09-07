import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {DateUtilsService} from '../../services/date-utils.service';
import {RideService} from '../../services/ride.service';
import {AuthService} from '../../services/auth.service';
import {Ride} from '../../Models/Ride';
import {WebSocketAttendanceService} from '../../services/web-socket-attendance.service';
import {DomSanitizer} from '@angular/platform-browser';
import {CsvDataService} from "../../utils/misc";

@Component({
  selector: 'app-attendance-display',
  templateUrl: './attendance-display.component.html',
  styleUrls: ['./attendance-display.component.css']
})
export class AttendanceDisplayComponent implements OnInit, OnDestroy {
  date: string;
  rides: Array<Ride> = [];
  rideJson: string;
  curPage = 0;
  downloadJsonHref;

  csvOptions = {
    fieldSeparator: ',',
    quoteStrings: '"',
    decimalseparator: '.',
    showLabels: false,
    headers: [],
    showTitle: true,
    title: 'Ride Csv',
    useBom: false,
    removeNewLines: true,
    keys: ['id', 'date', 'direction', 'rideBookingStatus', 'locked', 'latestStop', 'latestStopId', 'latestStopTime',
      'lineId', 'lineName', 'stopList']
  };

  constructor(private route: ActivatedRoute, private router: Router, private dateService: DateUtilsService,
              private rideService: RideService, private auth: AuthService,
              private wsaService: WebSocketAttendanceService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
    // Retrieve the date from the params (currently not used)
    this.route.params.subscribe((params: Params) => {
      this.date = params.from;
    });

    // If the date is null, set is to the current date
    if (this.date == null) {
      this.date = this.dateService.getCurrentDate();
    }

    this.rides = this.route.snapshot.data.rides;
    this.generateDownloadRideJsonUri();

    // Connect to the web socket
    if (this.rides.length > 0) {
      this.wsaService.setup(this);
      this.wsaService.connect(this.rides[0].id);
    }
  }

  ngOnDestroy() {
    this.wsaService.disconnect();
  }

  getRides() {
    this.rideService.getLockedRidesFromUserAndDate(this.auth.currentUserValue.id, this.date).subscribe(
      (data) => {
        this.rides = data;
        this.generateDownloadRideJsonUri();
      },
      (error) => {console.log(error); },
      () => console.log('Done building rides data structure')
    );
  }

  changePage(page) {
    this.curPage = page.pageIndex;
    this.generateDownloadRideJsonUri();

    // Synchronize the web service to the new page
    this.wsaService.disconnect();
    this.wsaService.connect(this.rides[this.curPage].id);
  }

  onBackClick() {
    this.router.navigate(['attendance', 'selection']);
  }

  onAttendanceChanged() {
    this.getRides();
    this.wsaService.send();
  }

  generateDownloadRideJsonUri() {
    this.rideJson = JSON.stringify(this.rides[this.curPage]);
    this.downloadJsonHref = this.sanitizer.bypassSecurityTrustUrl('data:text/json;charset=UTF-8,' +
      encodeURIComponent(this.rideJson));
  }

  onDownloadCsvClick(event: Event) {
    event.preventDefault();

    const curRide: any = this.rides[this.curPage];
    curRide.stopList = JSON.stringify(curRide.stopList);

    CsvDataService.exportToCsv('ride-' + curRide.id + '.csv',
      [curRide]);
  }
}
