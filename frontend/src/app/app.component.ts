import { Component } from '@angular/core';
import {CurrentUser} from './Models/currentUser';
import {Router} from '@angular/router';
import {AuthService} from './services/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  currentUser: CurrentUser;

  constructor(private router: Router, private authenticationService: AuthService) {
    this.authenticationService.currentUser
      .subscribe(x => this.currentUser = x);
  }

}
