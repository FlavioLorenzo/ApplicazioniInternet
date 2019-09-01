import {RouterModule, Routes} from '@angular/router';

import {LoginComponent} from './login/login.component';
import {AuthGuard as AuthGuard} from './guards/auth.guard';
import {LogoutComponent} from './logout/logout.component';
import {RegisterComponent} from './register/register.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserScreenComponent } from './user-screen/user-screen.component';
import { ChildrenListComponent } from './children-list/children-list.component';
import { ChildrenScreenComponent } from './children-screen/children-screen.component';

import {ReservationComponent} from './reservation/reservation.component';
import {SelectionComponent} from './reservation/selection/selection.component';
import {ReservationRideDisplayComponent} from './reservation/reservation-ride-display/reservation-ride-display.component';
import {AttendancePageComponent} from './attendance-page/attendance-page.component';
import {AttendanceDateLineSelectionComponent} from './attendance-page/attendance-date-line-selection/attendance-date-line-selection.component';
import {AttendanceDisplayComponent} from './attendance-page/attendance-display/attendance-display.component';
import {ShiftDefinitionDateLineSelectionComponent} from './shift-definition-page/shift-definition-date-line-selection/shift-definition-date-line-selection.component';
import {ShiftDefinitionRideDisplayComponent} from './shift-definition-page/shift-definition-ride-display/shift-definition-ride-display.component';
import {ShiftDefinitionPageComponent} from './shift-definition-page/shift-definition-page.component';
import {ShiftConvalidationPageComponent} from "./shift-convalidation-page/shift-convalidation-page.component";
import {AvailableShiftsDisplayComponent} from "./shift-convalidation-page/available-shifts-display/available-shifts-display.component";
import {ShowAvailabilitiesComponent} from "./shift-convalidation-page/show-availabilities/show-availabilities.component";

const appRoutes: Routes = [
  /*{
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },*/
  {
    path: 'attendance',
    component: AttendancePageComponent,
    children: [
      { path: '', component: AttendanceDisplayComponent },
      { path: 'line/:id/:from', component: AttendanceDisplayComponent },
      { path: 'selection', component: AttendanceDateLineSelectionComponent }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'reservation',
    component: ReservationComponent,
    children: [
      { path: '', component: SelectionComponent },
      { path: ':child/:line/:from', component: ReservationRideDisplayComponent }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'shift-definition',
    component: ShiftDefinitionPageComponent,
    children: [
      { path: '', component: ShiftDefinitionDateLineSelectionComponent },
      { path: 'line/:id/:from', component: ShiftDefinitionRideDisplayComponent }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'shift-consolidation',
    component: ShiftConvalidationPageComponent,
    children: [
      { path: '', component: AvailableShiftsDisplayComponent },
      { path: ':rideId', component: ShowAvailabilitiesComponent }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register/:code',
    component: RegisterComponent
  },
  {
    path: 'logout',
    component: LogoutComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'users',
    component: UserScreenComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'children',
    component: ChildrenScreenComponent,
    canActivate: [AuthGuard]
  },
  { // Otherwise redirect to home
    path: '**',
    redirectTo: 'reservation'
  }
];

export const appRoutingModule = RouterModule.forRoot(appRoutes);
