// BusLineStop class model

// BusStop models
import {BusStop} from './BusLineStop';
import {Line} from './Line';

export class Ride {
  id: number;
  date: string;
  direction: boolean;
  rideBookingStatus: string;
  locked: boolean;
  latestStop: string;
  latestStopId: number;
  latestStopTime: string;
  lineId: number;
  lineName: string;
  stopList: BusStop[];
}
