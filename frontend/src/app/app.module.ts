import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {MatDatepickerModule} from '@angular/material/datepicker';

import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import { JwtInterceptor } from 'src/app/helpers/jwt.interceptor';
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
import { LinesComponent } from './lines/lines.component';
import { AttendanceWrapperComponent } from './attendance-wrapper/attendance-wrapper.component';
import {MatFormFieldModule, MatInputModule, MatNativeDateModule} from '@angular/material';
import { DatepickerComponent } from './datepicker/datepicker.component';

@NgModule({
  declarations: [
    AppComponent,
    AttendanceComponent,
    HomeComponent,
    LoginComponent,
    HeaderComponent,
    LogoutComponent,
    LinesComponent,
    AttendanceWrapperComponent,
    DatepickerComponent
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
    MatInputModule
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
    MatDatepickerModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
