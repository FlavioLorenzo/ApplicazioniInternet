import { Component, OnInit } from '@angular/core';
import {RideAvailabilities, UserAvailabilityInfo} from '../ride-availabilities';
import {ActivatedRoute, Params} from '@angular/router';
import {RideService} from '../../services/ride.service';
import {Location} from '@angular/common';
import {AvailabilityService} from '../../services/availability.service';
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-show-availabilities',
  templateUrl: './show-availabilities.component.html',
  styleUrls: ['./show-availabilities.component.css']
})
export class ShowAvailabilitiesComponent implements OnInit {
  rideId: number;
  rideAvailabilities: RideAvailabilities;

  constructor(private route: ActivatedRoute, private rideService: RideService, private location: Location,
              private availabilityService: AvailabilityService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.rideId = params.rideId;
    });

    this.rideAvailabilities = null;
    this.getRideAvailabilitiesInfo();
  }

  getRideAvailabilitiesInfo() {
    this.rideService
      .getRideAvailabilityInfo(
        this.rideId
        ).subscribe(
      (data) => {this.rideAvailabilities = data; },
      (error) => {console.log(error); },
      () => console.log('Done building data structure')
    );
  }

  onElementAreaClicked(user: UserAvailabilityInfo) {
    if (user.shiftStatus === 1) {
      this.availabilityService.changeStatus(user.availabilityId, 2).subscribe(
        (data) => { this.getRideAvailabilitiesInfo(); },
        (error) => {console.log(error); });
    } else {
      this.availabilityService.changeStatus(user.availabilityId, 1).subscribe(
        (data) => { this.getRideAvailabilitiesInfo(); },
        (error) => {console.log(error); });
    }
  }

  onCloseOpenButtonClick() {
    this.rideService.changeLockedStatus(this.rideId, !this.rideAvailabilities.ride.locked).subscribe(
      (data) => { this.getRideAvailabilitiesInfo(); },
      (error) => {
        console.log(error);
        this.snackBar.open('Cannot change the status of the ride.');
      }
      );
  }

  onBackClick() {
    this.location.back();
  }
}
