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
import {AttendanceComponent} from './attendance-page/attendance-display/attendance/attendance.component';
import {LoginComponent} from './login/login.component';
import { LogoutComponent } from './logout/logout.component';

import {ReservationsService} from './services/reservations.service';
import {LinesService} from './services/lines.service';
import {UsersService} from './services/users.service';
import {RegistrationService} from './services/registration.service';
import {RideService} from './services/ride.service';

import {
  MatCheckboxModule,
  MatDialogModule,
  MatFormFieldModule,
  MatInputModule,
  MatNativeDateModule,
  MatSortModule,
  MatTableModule,
  MatRadioModule
} from '@angular/material';
import { DatepickerComponent } from './datepicker/datepicker.component';
// tslint:disable-next-line:max-line-length
import {DialogBoxPickNotBookedUserComponent} from './attendance-page/attendance-display/attendance/dialog-box-pick-not-booked-user.component';
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
import { MatProgressSpinnerModule, MatSnackBarModule, MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material';

import { ReservationComponent } from './reservation/reservation.component';
import { SelectLinesComponent } from './select-lines/select-lines.component';
import { SelectionComponent } from './reservation/selection/selection.component';
import { ReservationRideDisplayComponent } from './reservation/reservation-ride-display/reservation-ride-display.component';
import { ReservationRideComponent } from './reservation/reservation-ride-display/reservation-ride/reservation-ride.component';
import { ModifyStopPopupComponent } from './modify-stop-popup/modify-stop-popup.component';
import { SelectAvailableStopsComponent } from './select-available-stops/select-available-stops.component';
import { DateLineSelectionPageComponent } from './date-line-selection-page/date-line-selection-page.component';
import { AttendancePageComponent } from './attendance-page/attendance-page.component';
// tslint:disable-next-line:max-line-length
import { AttendanceDateLineSelectionComponent } from './attendance-page/attendance-date-line-selection/attendance-date-line-selection.component';
import { AttendanceDisplayComponent } from './attendance-page/attendance-display/attendance-display.component';
import { ShiftDefinitionPageComponent } from './shift-definition-page/shift-definition-page.component';
// tslint:disable-next-line:max-line-length
import { ShiftDefinitionDateLineSelectionComponent } from './shift-definition-page/shift-definition-date-line-selection/shift-definition-date-line-selection.component';
// tslint:disable-next-line:max-line-length
import { ShiftDefinitionRideDisplayComponent } from './shift-definition-page/shift-definition-ride-display/shift-definition-ride-display.component';
// tslint:disable-next-line:max-line-length
import { ShiftDefinitionRideComponent } from './shift-definition-page/shift-definition-ride-display/shift-definition-ride/shift-definition-ride.component';
import {AvailabilityService} from './services/availability.service';
import { ShiftConvalidationPageComponent } from './shift-convalidation-page/shift-convalidation-page.component';
import { AvailableShiftsDisplayComponent } from './shift-convalidation-page/available-shifts-display/available-shifts-display.component';
import { DailyRidesComponent } from './shift-convalidation-page/available-shifts-display/daily-rides/daily-rides.component';
import { ShowAvailabilitiesComponent } from './shift-convalidation-page/show-availabilities/show-availabilities.component';
// tslint:disable-next-line:max-line-length
import { FilterAvailabilitiesPopupComponent } from './shift-convalidation-page/available-shifts-display/filter-availabilities-popup/filter-availabilities-popup.component';
import { SelectChildComponent } from './select-child/select-child.component';
import {AttendanceResolverService} from './resolvers/attendance-resolver.service';
import {ReservationResolverService} from './resolvers/reservation-resolver.service';
import {AvailabilitiesResolverService} from './resolvers/availabilities-resolver.service';
import {ShiftConsolidationResolverService} from './resolvers/shift-consolidation-resolver.service';
import {WebSocketAttendanceService} from './services/web-socket-attendance.service';

@NgModule({
  declarations: [
    AppComponent,
    AttendanceComponent,
    HomeComponent,
    LoginComponent,
    RegisterComponent,
    HeaderComponent,
    LogoutComponent,
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
    ReservationComponent,
    ModifyStopPopupComponent,
    SelectLinesComponent,
    SelectionComponent,
    ReservationRideDisplayComponent,
    ReservationRideComponent,
    ModifyStopPopupComponent,
    SelectAvailableStopsComponent,
    DateLineSelectionPageComponent,
    AttendancePageComponent,
    AttendanceDateLineSelectionComponent,
    AttendanceDisplayComponent,
    ShiftDefinitionPageComponent,
    ShiftDefinitionDateLineSelectionComponent,
    ShiftDefinitionRideDisplayComponent,
    ShiftDefinitionRideComponent,
    ShiftConvalidationPageComponent,
    AvailableShiftsDisplayComponent,
    DailyRidesComponent,
    ShowAvailabilitiesComponent,
    FilterAvailabilitiesPopupComponent,
    SelectChildComponent
  ],
  entryComponents: [
    DialogBoxPickNotBookedUserComponent,
    UserDetailsDialogComponent,
    ModifyStopPopupComponent,
    FilterAvailabilitiesPopupComponent,
    ReservationComponent,
    SelectLinesComponent,
    SelectionComponent,
    ReservationRideDisplayComponent,
    ReservationRideComponent,
    ModifyStopPopupComponent,
    SelectAvailableStopsComponent,
    DateLineSelectionPageComponent,
    AttendancePageComponent,
    AttendanceDateLineSelectionComponent,
    AttendanceDisplayComponent,
    ShiftDefinitionPageComponent,
    ShiftDefinitionDateLineSelectionComponent,
    ShiftDefinitionRideDisplayComponent,
    ShiftDefinitionRideComponent
  ],
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
    MatCheckboxModule,
    MatProgressSpinnerModule,
    MatSnackBarModule,
    MatRadioModule
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
    MatDatepickerModule,
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 2000}},
    AvailabilityService,
    MatDatepickerModule,
    RideService,
    AttendanceResolverService,
    ReservationResolverService,
    AvailabilitiesResolverService,
    ShiftConsolidationResolverService,
    WebSocketAttendanceService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
