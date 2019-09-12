import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {CurrentUser} from '../Models/currentUser';
import {Router} from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  currentUser: CurrentUser;
  constructor(private auth: AuthService, private router: Router) { }

  ngOnInit() {
    this.currentUser = this.auth.currentUserValue;
  }

  isCurrentRoleAdmitted(admittedRoles: number[]) {
    const currentRole: number = +this.currentUser.role.id_role;

    for (const role of admittedRoles) {
      if (currentRole === +role) {
        return true;
      }
    }

    return false;
  }

  onButtonClick(where: string) {
    this.router.navigate([where]);
  }
}
