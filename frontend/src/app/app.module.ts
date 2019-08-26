import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatSelectModule} from '@angular/material/select';
import {MatButtonModule} from '@angular/material/button'; 

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';

import { JwtInterceptor } from './helpers/jwt.interceptor';
import { AuthService } from './services/auth.service';
import { AuthGuard as AuthGuard } from './guards/auth.guard';

import {appRoutingModule} from './app.routing';
import {HeaderComponent} from './header/header.component';
import {HomeComponent} from './home/home.component';
import {AppComponent} from './app.component';
import {AttendanceComponent} from './attendance/attendance.component';
import {LoginComponent} from './login/login.component';
import { LogoutComponent } from './logout/logout.component';

import {ReservationsService} from './services/reservations.service';
import {LinesService} from './services/lines.service';
import {UsersService} from './services/users.service';
import {RegistrationService} from './services/registration.service';

import { LinesComponent } from './lines/lines.component';
import { AttendanceWrapperComponent } from './attendance-wrapper/attendance-wrapper.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { DatepickerComponent } from './datepicker/datepicker.component';
import {DialogBoxPickNotBookedUserComponent} from './attendance/dialog-box-pick-not-booked-user.component';
import {RegisterComponent} from './register/register.component';
import { UserListComponent } from './user-list/user-list.component';
import { UserDetailsDialogComponent } from './user-details-dialog/user-details-dialog.component';
import { LabelRolePipe } from './role-label.pipe';
import { UserRegistrationComponent } from './user-registration/user-registration.component';
import { UserScreenComponent } from './user-screen/user-screen.component';
import { LineSelectorComponent } from './line-selector/line-selector.component';
import { ChildrenScreenComponent } from './children-screen/children-screen.component';
import { ChildrenListComponent } from './children-list/children-list.component';
import { ChildrenRegistrationComponent } from './children-registration/children-registration.component';


@NgModule({
  declarations: [
    AppComponent,
    AttendanceComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    HeaderComponent,
    LogoutComponent,
    LinesComponent,
    AttendanceWrapperComponent,
    DatepickerComponent,
    DialogBoxPickNotBookedUserComponent,
    UserListComponent,
    UserDetailsDialogComponent,
    LabelRolePipe,
    UserRegistrationComponent,
    UserScreenComponent,
    LineSelectorComponent,
    ChildrenScreenComponent,
    ChildrenListComponent,
    ChildrenRegistrationComponent,
  ],
  entryComponents: [DialogBoxPickNotBookedUserComponent, UserDetailsDialogComponent],
  imports: [
    BrowserModule,
    MatListModule,
    MatCardModule,
    MatIconModule,
    MatPaginatorModule,
    BrowserAnimationsModule,
    appRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDialogModule,
    FormsModule,
    MatTableModule,
    MatSortModule,
    MatCheckboxModule
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    AuthGuard,
    ReservationsService,
    LinesService,
    UsersService,
    RegistrationService,
    MatDatepickerModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
