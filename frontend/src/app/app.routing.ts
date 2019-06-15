import {RouterModule, Routes} from '@angular/router';

import { AttendanceComponent } from './attendance/attendance.component';
import { HomeComponent } from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuardService as AuthGuard} from './jwt-authentication/authguard.service';
import {LogoutComponent} from './logout/logout.component';
import {RegisterComponent} from "./register/register.component";

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
    redirectTo: ''
  }
];

export const appRoutingModule = RouterModule.forRoot(appRoutes);
