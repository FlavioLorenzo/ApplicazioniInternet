// BusLineStop class model

// BusStop models
import {User} from './User';

export class BusStop {
  id: number;
  name: string;
  direction: boolean;
  line: number;
  arrivaltime: string;
  passengers: User[];
}
