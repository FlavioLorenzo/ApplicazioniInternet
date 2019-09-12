import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-password-reset-request',
  templateUrl: './password-reset-request.component.html',
  styleUrls: ['./password-reset-request.component.css']
})
export class PasswordResetRequestComponent implements OnInit {

  form: FormGroup;
  submitted = false;
  invalidCredentials: boolean;

  constructor(private fb: FormBuilder,
              private authenticationService: AuthService,
              private router: Router,
              private snackBar: MatSnackBar) {
  }

  ngOnInit() {
    if (this.authenticationService.currentUserValue) {
      this.router.navigate(['/']); // If user is logged i go directly to the homepage
    }

    this.invalidCredentials = false;
    this.form = this.fb.group({
      email: ['', Validators.required]
    });
  }

  onResetPassword() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    const val = this.form.value;
    if (val.email) {
      this.authenticationService.recoverPassword(val.email)
        .subscribe(
          data => {
            this.snackBar.open('An email has been sent to your email address. Please check your inbox and follow ' +
              'the provided link to reset your password.');
            this.invalidCredentials = false;
            this.router.navigate(['/']); //I send a redirect after completing the procedure
          },
          error => {
            console.error(error);
            this.invalidCredentials = true;
          });
    }
  }
}
