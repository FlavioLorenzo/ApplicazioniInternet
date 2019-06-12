import { Component, OnInit } from '@angular/core';
import {AuthService} from '../jwt-authentication/auth.service';
import {CurrentUser} from '../Models/currentUser';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  currentUser: CurrentUser;

  constructor(private authenticationService: AuthService) {
    this.authenticationService.currentUser
      .subscribe(x => this.currentUser = x);
  }

  ngOnInit() {
  }
}
