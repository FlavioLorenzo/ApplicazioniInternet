import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatPaginatorModule } from '@angular/material/paginator';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import { JwtInterceptor } from 'src/app/jwt-authentication/jwt.interceptor';
import { AuthService } from './jwt-authentication/auth.service';
import { AuthGuardService as AuthGuard } from './jwt-authentication/authguard.service';

import {appRoutingModule} from './app.routing';
import {HeaderComponent} from './header/header.component';
import {HomeComponent} from './home/home.component';
import {AppComponent} from './app.component';
import {AttendanceComponent} from './attendance/attendance.component';
import {LoginComponent} from './login/login.component';
import { LogoutComponent } from './logout/logout.component';

@NgModule({
  declarations: [
    AppComponent,
    AttendanceComponent,
    HomeComponent,
    LoginComponent,
    HeaderComponent,
    LogoutComponent
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
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    },
    AuthGuard
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
