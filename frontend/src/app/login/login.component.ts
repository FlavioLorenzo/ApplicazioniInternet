import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../jwt-authentication/auth.service';
import {throwError} from "rxjs";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [AuthService]
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) {
      this.form = this.fb.group({
        email: ['', Validators.required],
        password: ['', Validators.required]
      });
  }

  login() {
    const val = this.form.value;
    if (val.email && val.password) {
      this.authService.login(val.email, val.password)
        .subscribe((res) => {
          if (res === null) { throwError('Bad Response'); }
          this.router.navigateByUrl('/');
        },
        () => {
          console.log('Wrong credentials');
        });
    }
  }

  ngOnInit() {
  }
}
