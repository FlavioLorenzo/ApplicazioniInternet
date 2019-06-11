import {RouterModule, Routes} from '@angular/router';

import { AttendanceComponent } from './attendance/attendance.component';
import { HomeComponent } from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuardService as AuthGuard} from './jwt-authentication/authguard.service';

const appRoutes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'attendance',
    component: AttendanceComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  { // Otherwise redirect to home
    path: '**',
    redirectTo: ''
  }
];

export const appRoutingModule = RouterModule.forRoot(appRoutes);
