import { Role } from './currentUser';

// User class model

export class User {
  id_user: number;
  first_name: string;
  last_name: string;
  role: Role;
  email: string;
  phone: string;
  picked: boolean;
  reservationId: number;
}
