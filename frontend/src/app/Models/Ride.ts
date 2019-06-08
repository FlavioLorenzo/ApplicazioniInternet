// BusLineStop class model

// BusStop models
import {BusStop} from './BusLineStop';

export class Ride {
  id: number;
  date: string;
  direction: string;
  stopList: BusStop[];
}
