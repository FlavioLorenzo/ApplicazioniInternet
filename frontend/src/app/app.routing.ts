import {RouterModule, Routes} from '@angular/router';

import { AttendanceComponent } from './attendance/attendance.component';
import { HomeComponent } from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuard as AuthGuard} from './guards/auth.guard';
import {LogoutComponent} from './logout/logout.component';
import {AttendanceWrapperComponent} from './attendance-wrapper/attendance-wrapper.component';
import {RegisterComponent} from './register/register.component';
import {ReservationComponent} from './reservation/reservation.component';
import {SelectionComponent} from "./reservation/selection/selection.component";
import {ReservationRideDisplayComponent} from "./reservation/reservation-ride-display/reservation-ride-display.component";

const appRoutes: Routes = [
  /*{
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },*/
  {
    path: 'attendance',
    component: AttendanceWrapperComponent ,
    canActivate: [AuthGuard]
  },
  {
    path: 'reservation',
    component: ReservationComponent,
    children: [
      { path: '', component: SelectionComponent },
      { path: 'line/:id/:from', component: ReservationRideDisplayComponent }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'logout',
    component: LogoutComponent,
    canActivate: [AuthGuard]
  },
  { // Otherwise redirect to home
    path: '**',
    redirectTo: 'attendance'
  }
];

export const appRoutingModule = RouterModule.forRoot(appRoutes);
