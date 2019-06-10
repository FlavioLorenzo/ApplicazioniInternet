import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

import { MatListModule } from '@angular/material/list';
import { MatCardModule } from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';
import {MatPaginatorModule} from '@angular/material/paginator';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { AttendanceComponent } from './attendance/attendance.component';
import { HomeComponent } from './home/home.component';
import {LoginComponent} from './login/login.component';
import {ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';
import { JwtInterceptor } from 'src/app/jwt-authentication/jwt.interceptor';
import {AuthService} from './jwt-authentication/auth.service';

const appRoutes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'attendance', component: AttendanceComponent},
  {path: 'login', component: LoginComponent},
];

@NgModule({
  declarations: [
    AppComponent,
    AttendanceComponent,
    HomeComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    MatListModule,
    MatCardModule,
    MatIconModule,
    MatPaginatorModule,
    BrowserAnimationsModule,
    RouterModule.forRoot(appRoutes),
    ReactiveFormsModule,
    HttpClientModule,
  ],
  providers: [
    AuthService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: JwtInterceptor,
      multi: true,
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
