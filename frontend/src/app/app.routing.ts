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
// tslint:disable-next-line:max-line-length
import {AttendanceDateLineSelectionComponent} from './attendance-page/attendance-date-line-selection/attendance-date-line-selection.component';
import {AttendanceDisplayComponent} from './attendance-page/attendance-display/attendance-display.component';
// tslint:disable-next-line:max-line-length
import {ShiftDefinitionDateLineSelectionComponent} from './shift-definition-page/shift-definition-date-line-selection/shift-definition-date-line-selection.component';
// tslint:disable-next-line:max-line-length
import {ShiftDefinitionRideDisplayComponent} from './shift-definition-page/shift-definition-ride-display/shift-definition-ride-display.component';
import {ShiftDefinitionPageComponent} from './shift-definition-page/shift-definition-page.component';
import {ShiftConvalidationPageComponent} from './shift-convalidation-page/shift-convalidation-page.component';
import {AvailableShiftsDisplayComponent} from './shift-convalidation-page/available-shifts-display/available-shifts-display.component';
import {ShowAvailabilitiesComponent} from './shift-convalidation-page/show-availabilities/show-availabilities.component';
import {AttendanceResolverService} from './resolvers/attendance-resolver.service';
import {ReservationResolverService} from './resolvers/reservation-resolver.service';
import {AvailabilitiesResolverService} from './resolvers/availabilities-resolver.service';
import {ShiftConsolidationResolverService} from './resolvers/shift-consolidation-resolver.service';
import {NotificationsComponent} from './notifications/notifications.component';
import { ManagedLineResolverService as ManagedLineResolverService } from './resolvers/managed-line-resolver.service';
import {PasswordResetComponent} from './password-reset/password-reset.component';
import {PasswordResetRequestComponent} from './password-reset-request/password-reset-request.component';
import {HomeComponent} from './home/home.component';

const appRoutes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'register/:code',
    component: RegisterComponent
  },
  {
    path: 'recover',
    component: PasswordResetRequestComponent
  },
  {
    path: 'recover/:token',
    component: PasswordResetComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'notifications',
    component: NotificationsComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'attendance',
    component: AttendancePageComponent,
    children: [
      {
        path: '',
        component: AttendanceDisplayComponent,
        resolve: {rides: AttendanceResolverService }
      }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'reservation',
    component: ReservationComponent,
    children: [
      { path: '', component: SelectionComponent },
      {
        path: ':child/:line/:from',
        component: ReservationRideDisplayComponent,
        resolve: {info: ReservationResolverService}
      }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'shift-definition',
    component: ShiftDefinitionPageComponent,
    children: [
      { path: '', component: ShiftDefinitionDateLineSelectionComponent },
      {
        path: 'line/:id/:from',
        component: ShiftDefinitionRideDisplayComponent,
        resolve: {info: AvailabilitiesResolverService }
      }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'shift-consolidation',
    component: ShiftConvalidationPageComponent,
    children: [
      {
        path: '',
        component: AvailableShiftsDisplayComponent,
        resolve: {rides: ShiftConsolidationResolverService }
      },
      {
        path: ':rideId',
        component: ShowAvailabilitiesComponent
      }
    ],
    canActivate: [AuthGuard]
  },
  {
    path: 'logout',
    component: LogoutComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'users',
    component: UserScreenComponent,
    canActivate: [AuthGuard],
    resolve: {managedLines: ManagedLineResolverService }
  },
  {
    path: 'children',
    component: ChildrenScreenComponent,
    canActivate: [AuthGuard]
  },
  { // Otherwise redirect to home
    path: '**',
    redirectTo: ''
  }
];

export const appRoutingModule = RouterModule.forRoot(appRoutes);
