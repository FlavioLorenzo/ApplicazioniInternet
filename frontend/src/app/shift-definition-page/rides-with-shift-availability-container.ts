export class RidesWithShiftAvailabilityContainer {
  // tslint:disable-next-line:variable-name
  date: string;
  forthRide: RideWithShiftAvailability;
  backRide: RideWithShiftAvailability;

  constructor(date: string,
              forthRide: RideWithShiftAvailability,
              backRide: RideWithShiftAvailability) {
    this.date = date;
    this.forthRide = forthRide;
    this.backRide = backRide;
  }
}

export class RideWithShiftAvailability {
  id: number;
  date: string;
  direction: boolean;
  lineId: number;
  lineName: string;
  rideBookingStatus: string;
  locked: boolean;
  availability: ShiftAvailabilityOfRide;

  constructor(id: number,
              date: string,
              direction: boolean,
              lineId: number,
              lineName: string,
              rideBookingStatus: string,
              locked: boolean) {
    this.id = id;
    this.date = date;
    this.direction = direction;
    this.lineId = lineId;
    this.lineName = lineName;
    this.rideBookingStatus = rideBookingStatus;
    this.locked = locked;
  }
}

export class ShiftAvailabilityOfRide {
  availabilityId: number;
  stopId: number;
  stopName: string;
  arrivalTime: string;
  shiftStatus: number;

  constructor(availabilityId: number,
              stopId: number,
              stopName: string,
              arrivalTime: string,
              shiftStatus: number) {
    this.availabilityId = availabilityId;
    this.stopId = stopId;
    this.stopName = stopName;
    this.arrivalTime = arrivalTime;
    this.shiftStatus = shiftStatus;
  }
}
