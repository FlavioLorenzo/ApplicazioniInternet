import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../jwt-authentication/auth.service';
import {first, last, take} from 'rxjs/operators';
import {HttpRegistrationService} from '../httpRegistrationService/httpRegistration.service';


// @ts-ignore
// @ts-ignore
// @ts-ignore
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [HttpRegistrationService]
})

export class RegisterComponent implements OnInit {
  form: FormGroup;
  submitted = false;

  constructor(private fb: FormBuilder, private httpRegistrationService: HttpRegistrationService,
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

  onRegister() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    const val = this.form.value;
    if (val.first && val.second && val.phone && val.email && val.password && val.passwordConfirm) {
      // Questa funzione va modificata con il servizio corretto che sarebbe httpRegistrationService
      this.httpRegistrationService.Register(val.email, val.password, val.passwordConfirm, val.first, val.last)
        .pipe(first())
        .subscribe(
          data => {
            console.log('Redirecting to home');
            this.router.navigate(['/']);
          },
          error => {
            console.log('wrong credentials');
          });
    }



  }

  ngOnInit(): void {
  }

}


