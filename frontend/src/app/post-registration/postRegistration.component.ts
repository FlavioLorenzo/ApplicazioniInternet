import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegistrationPostBody, RegistrationService} from '../services/registration.service';
import {first, take} from 'rxjs/operators';
import {ReservationPostBody} from '../services/reservations.service';


// @ts-ignore
// @ts-ignore
// @ts-ignore
@Component({
  selector: 'app-register',
  templateUrl: './postRegistration.component.html',
  styleUrls: ['./postRegistration.component.css'],
  providers: [RegistrationService]
})

export class PostRegistrationComponent implements OnInit {
  form: FormGroup;
  submitted = false;

  // authenticationService dovrebbe essere httpRegistrationService appena capisco come passarlo
  constructor(private fb: FormBuilder,
              private registrationService: RegistrationService,
              private router: Router
  ) {
    this.form = this.fb.group({
      first: ['', Validators.required, Validators.maxLength(20)],
      last: ['', Validators.required, Validators.maxLength(20)],
      phone: ['', Validators.required, Validators.minLength(2), Validators.maxLength(20)],
      email: ['', Validators.required, Validators.email, Validators.maxLength(30)],
      password: ['', Validators.required, Validators.minLength(4), Validators.maxLength(20)],
      passwordConfirm: ['', Validators.required, Validators.minLength(4), Validators.maxLength(20)]
    });
  }

  onRegister() {
    this.submitted = true;

    if (this.form.invalid) {
      console.log('This form is invalid');
      return;
    }

    const val = this.form.value;
    console.log(val);
    if (val.first && val.last && val.phone && val.email && val.password && val.passwordConfirm) {
      const rpb = new RegistrationPostBody(val.email, val.password, val.passwordConfirm, val.first, val.last);

      this.registrationService.register(rpb)
        .pipe(first())
        .subscribe(
          data => {
            console.log('Redirecting to login');
            this.router.navigate(['/login']);
          },
          error => {
            console.error(error);
            console.log('Something went wrong');
          });
    }
  }

  ngOnInit(): void {
  }

}


