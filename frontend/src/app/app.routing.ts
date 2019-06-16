import {RouterModule, Routes} from '@angular/router';

import { AttendanceComponent } from './attendance/attendance.component';
import { HomeComponent } from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuard as AuthGuard} from './guards/auth.guard';
import {LogoutComponent} from './logout/logout.component';
import {AttendanceWrapperComponent} from './attendance-wrapper/attendance-wrapper.component';
import {RegisterComponent} from './register/register.component';

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
