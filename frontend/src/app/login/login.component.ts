import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../jwt-authentication/auth.service';
import {first, take} from 'rxjs/operators';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [AuthService]
})

export class LoginComponent implements OnInit {
  form: FormGroup;
  submitted = false;

  constructor(private fb: FormBuilder,
              private authenticationService: AuthService,
              private router: Router
  ) {
      if (this.authenticationService.currentUserValue) {
        this.router.navigate(['/']); // If user is logged i go directly to the homepage
      }

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
            console.log('Redirecting to home');
            this.router.navigate(['/']);
        },
        error => { console.log('wrong credentials'); });
    }
  }

  onRegister() {
    this.router.navigate(['/register']);
  }

  ngOnInit() {
  }
}

