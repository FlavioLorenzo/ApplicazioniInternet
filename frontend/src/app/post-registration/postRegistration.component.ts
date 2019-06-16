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
  constructor(private registrationService: RegistrationService,
              private router: Router
  ) {

  }

  onRegister() {
    this.submitted = true;

    if (this.form.invalid) {
      console.log('This form is invalid');
      return;
    }

    /*
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

     */
  }

  ngOnInit(): void {
  }

}


