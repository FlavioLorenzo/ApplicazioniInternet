// BusLineStop class model

// BusStop models
import {Child} from './Child';

export class BusStop {
  id: number;
  name: string;
  direction: boolean;
  line: number;
  arrivalTime: string;
  passengers: Child[];
}
