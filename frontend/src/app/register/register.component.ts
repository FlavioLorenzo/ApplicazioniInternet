import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {RegistrationPostBody, RegistrationService} from '../services/registration.service';
import {first, take} from 'rxjs/operators';

// @ts-ignore
// @ts-ignore
// @ts-ignore
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
  providers: [RegistrationService]
})

export class RegisterComponent implements OnInit {
  form: FormGroup;
  submitted = false;
  countErrors = 0;

  // authenticationService dovrebbe essere httpRegistrationService appena capisco come passarlo
  constructor(private fb: FormBuilder,
              private registrationService: RegistrationService,
              private router: Router
  ) {
    this.form = this.fb.group({
      first: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(256)]],
      last: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(256)]],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(255), Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(32)]],
      passwordConfirm: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(32)]]
    });
  }

  onRegister() {
    this.submitted = true;
    if (this.form.invalid) {
      const mexBox = document.getElementById('ai-mex-box');

      if (count === 0) {
        mexBox.classList.add('ai-mex-box-error');
      }

      const toRemove = document.getElementById('ai-email-already-used');
      if (toRemove) {
        mexBox.removeChild(toRemove);
        count--;
      }

      const node = document.createElement('div');
      node.appendChild(document.createTextNode('There is an error in the form.'));
      mexBox.appendChild(node);
      count++;
      return;
    }

    const val = this.form.value;
    if (val.password !== val.passwordConfirm) {
      const mexBox = document.getElementById('ai-mex-box');

      if (count === 0) {
        mexBox.classList.add('ai-mex-box-error');
      }

      const toRemove = document.getElementById('ai-email-already-used');
      if (toRemove) {
        mexBox.removeChild(toRemove);
        count--;
      }

      const node = document.createElement('div');
      node.appendChild(document.createTextNode('There is an error in the form.'));
      mexBox.appendChild(node);
      count++;
      return;
    }

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

  onEmailBlur(email: string) {
    this.registrationService.checkEmail(email)
      .pipe(first())
      .subscribe(
        data => {
          const toRemove = document.getElementById('ai-email-already-used');
          const mexBox = document.getElementById('ai-mex-box');
          if (toRemove) {
            mexBox.removeChild(toRemove);
            count--;
            if (count === 0) {
              mexBox.classList.remove('ai-mex-box-error');
            }
          }
        },
        error => {
          const mexBox = document.getElementById('ai-mex-box');

          if (document.getElementById('ai-email-already-used')) {
            return;
          }

          if (count === 0) {
            mexBox.classList.add('ai-mex-box-error');
          }

          const node = document.createElement('div');
          node.setAttribute('id', 'ai-email-already-used');
          node.appendChild(document.createTextNode('This email is not valid or it already exists.'));
          mexBox.appendChild(node);

          count++;
        });
  }

  ngOnInit(): void {
  }

}


