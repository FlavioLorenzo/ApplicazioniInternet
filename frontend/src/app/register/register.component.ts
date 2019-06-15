import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../jwt-authentication/auth.service';
import {first, take} from 'rxjs/operators';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [AuthService]
})

export class RegisterComponent implements OnInit {
  form: FormGroup;
  submitted = false;

  constructor(private fb: FormBuilder,
              private router: Router
  ) {
      this.form = this.fb.group({
        first: ['', Validators.required],
        last: ['', Validators.required],
        phone: ['', Validators.required],
        email: ['', Validators.required],
        password: ['', Validators.required],
        passwordConfirm: ['', Validators.required]
      });
  }

  onLogin() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    const val = this.form.value;
    if ( val.first && val.second && val.phone && val.email && val.password && val.passwordConfirm) {

    }
  }

  ngOnInit() {
  }
}
