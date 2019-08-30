import {RideWithShiftAvailability} from '../shift-definition-page/rides-with-shift-availability-container';

export class RideAvailabilities {
  ride: RideWithShiftAvailability;
  available: Array<UserAvailabilityInfo>;
  confirmed: Array<UserAvailabilityInfo>;

  constructor(ride: RideWithShiftAvailability,
              available: Array<UserAvailabilityInfo>,
              confirmed: Array<UserAvailabilityInfo>) {
    this.ride = ride;
    this.available = available;
    this.confirmed = confirmed;
  }
}

export class UserAvailabilityInfo {
  availabilityId: number;
  userId: number;
  firstName: string;
  lastName: string;
  stopId: number;
  stopName: string;
  shiftStatus: number;
  arrivalTime: string;

  constructor(
               userId: number,
               availabilityId: number,
               firstName: string,
               lastName: string,
               stopId: number,
               stopName: string,
               shiftStatus: number,
               arrivalTime: string) {
    this.availabilityId = availabilityId;
    this.userId = userId;
    this.firstName = firstName;
    this.lastName = lastName;
    this.stopId = stopId;
    this.stopName = stopName;
    this.arrivalTime = arrivalTime;
    this.shiftStatus = shiftStatus;
  }
}
