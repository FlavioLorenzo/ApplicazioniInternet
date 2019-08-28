import {RouterModule, Routes} from '@angular/router';

import { AttendanceComponent } from './attendance/attendance.component';
import { HomeComponent } from './home/home.component';
import {LoginComponent} from './login/login.component';
import {AuthGuard as AuthGuard} from './guards/auth.guard';
import {LogoutComponent} from './logout/logout.component';
import {AttendanceWrapperComponent} from './attendance-wrapper/attendance-wrapper.component';
import {RegisterComponent} from './register/register.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserScreenComponent } from './user-screen/user-screen.component';
import { ChildrenListComponent } from './children-list/children-list.component';
import { ChildrenScreenComponent } from './children-screen/children-screen.component';


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
    redirectTo: 'attendance'
  }
];

export const appRoutingModule = RouterModule.forRoot(appRoutes);
