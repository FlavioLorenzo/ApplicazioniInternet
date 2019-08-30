export class RidesSummaryByDateContainer {
  // tslint:disable-next-line:variable-name
  date: string;
  rides: Array<RideSummary>;

  constructor(date: string,
              rides: Array<RideSummary>) {
    this.date = date;
    this.rides = rides;
  }
}

export class RideSummary {
  id: number;
  date: string;
  direction: boolean;
  lineId: number;
  lineName: string;
  rideBookingStatus: string;
  locked: boolean;
  coverage: string;

  constructor(id: number,
              date: string,
              direction: boolean,
              lineId: number,
              lineName: string,
              rideBookingStatus: string,
              locked: boolean,
              coverage: string) {
    this.id = id;
    this.date = date;
    this.direction = direction;
    this.lineId = lineId;
    this.lineName = lineName;
    this.rideBookingStatus = rideBookingStatus;
    this.locked = locked;
    this.coverage = coverage;
  }
}
