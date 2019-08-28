export class RidesWithReservationContainer {
  // tslint:disable-next-line:variable-name
  date: string;
  forthRide: RideWithReservation;
  backRide: RideWithReservation;

  constructor(date: string,
              forthRide: RideWithReservation,
              backRide: RideWithReservation) {
    this.date = date;
    this.forthRide = forthRide;
    this.backRide = backRide;
  }
}

export class RideWithReservation {
  id: number;
  date: string;
  direction: boolean;
  lineId: number;
  lineName: string;
  rideBookingStatus: string;
  locked: boolean;
  latestStop: string;
  latestStopTime: string;
  reservation: ReservationOfRide;

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

export class ReservationOfRide {
  reservationId: number;
  stopId: number;
  stopName: string;
  arrivalTime: string;

  constructor(reservationId: number,
              stopId: number,
              stopName: string,
              arrivalTime: string) {
    this.reservationId = reservationId;
    this.stopId = stopId;
    this.stopName = stopName;
    this.arrivalTime = arrivalTime;
  }
}
