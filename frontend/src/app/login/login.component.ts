import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {first, take} from 'rxjs/operators';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit {
  form: FormGroup;
  submitted = false;
  invalidCredentials: boolean;

  constructor(private fb: FormBuilder,
              private authenticationService: AuthService,
              private router: Router
  ) {
      if (this.authenticationService.currentUserValue) {
        this.router.navigate(['/']); // If user is logged i go directly to the homepage
      }

      this.invalidCredentials = false;
      this.form = this.fb.group({
        email: ['', Validators.required],
        password: ['', Validators.required]
      });
  }

  onLogin() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    const val = this.form.value;
    if (val.email && val.password) {
      this.authenticationService.login(val.email, val.password)
        .pipe(first())
        .subscribe(
          data => {
            // Provo a fare il redirect dall'auth service
        },
        error => {
            console.error(error);
            console.log('wrong credentials');
            this.invalidCredentials = true;
          });
    }
  }

  ngOnInit() {
  }
}

