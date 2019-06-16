import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../services/auth.service';
import {first, take} from 'rxjs/operators';


// @ts-ignore
// @ts-ignore
// @ts-ignore
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [AuthService]
})

export class RegisterComponent implements OnInit {
  form: FormGroup;
  submitted = false;

  // authenticationService dovrebbe essere httpRegistrationService appena capisco come passarlo
  constructor(private fb: FormBuilder, private authenticationService: AuthService,
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
      this.authenticationService.login(val.email, val.password)
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


