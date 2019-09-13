import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router, ActivatedRoute} from '@angular/router';
import {RegistrationPostBody, RegistrationService, CompleteUserPostBody} from '../services/registration.service';
import {first, take} from 'rxjs/operators';
import { User } from '../Models/User';
import { CurrentUser } from '../Models/currentUser';

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

  isLoading = true;                 // Waiting for server response
  hasError = true;                  // Response is negative - show error message
  registrationCompleted = false ;   // Registration is done

  pendingActivationCode: string; // The code used to complete the user
  displayName: string;
  reg = /^(\+39)?\d{8,12}$/;

  constructor(private fb: FormBuilder,
              private registrationService: RegistrationService,
              private router: Router,
              private route: ActivatedRoute
  ) {
    this.form = this.fb.group({
      phone: ['', [Validators.required, Validators.pattern(this.reg)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(32)]],
      passwordConfirm: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(32)]]
    });
  }

  onRegister() {
    this.submitted = true;
    if (this.form.invalid) {
      const mexBox = document.getElementById('ai-mex-box');

      if (this.countErrors === 0) {
        mexBox.classList.add('ai-mex-box-error');
      }

      const toRemove = document.getElementById('ai-generic-error');
      if (toRemove) {
        mexBox.removeChild(toRemove);
        this.countErrors--;
      }

      const node = document.createElement('div');
      node.setAttribute('id', 'ai-generic-error');
      node.appendChild(document.createTextNode('There is an error in your form.'));
      mexBox.appendChild(node);
      this.countErrors++;
      return;
    }

    const val = this.form.value;


    if (val.password !== val.passwordConfirm) {
      const mexBox = document.getElementById('ai-mex-box');

      if (this.countErrors === 0) {
        mexBox.classList.add('ai-mex-box-error');
      }

      const toRemove = document.getElementById('ai-generic-error');
      if (toRemove) {
        mexBox.removeChild(toRemove);
        this.countErrors--;
      }

      const node = document.createElement('div');
      node.setAttribute('id', 'ai-generic-error');
      node.appendChild(document.createTextNode('The two passwords are not equal.'));
      mexBox.appendChild(node);
      this.countErrors++;
      return;
    }


    if (val.phone && val.password && val.passwordConfirm) {
      // tslint:disable-next-line:max-line-length
      this.registrationService.completeRegistration(this.pendingActivationCode, new CompleteUserPostBody(val.password, val.passwordConfirm, val.phone))
        .pipe(first())
        .subscribe(
          data => {
            // TODO: SHOW CONFIRMATION TOAST
            console.log('Registration completed');
            console.log('Redirecting to home');
            this.router.navigate(['/']);
          },
          error => {
            console.error(error);
            console.log('Something went wrong');
          });
    }

  }

  ngOnInit(): void {

    // Getting the code from the url
    this.pendingActivationCode = this.route.snapshot.paramMap.get('code');

    if (this.pendingActivationCode) {

      this.registrationService.confirmRegistrationToken(this.pendingActivationCode)
      .subscribe(result => {
        const user: User = result;
        this.displayName = `${user.first_name} ${user.last_name}`;
        this.isLoading = false;
        this.hasError = false;
      }, error => {
        // Code is invalid
        this.hasError = true;
        this.isLoading = false;
      });

    } else {
      // No code found
      this.hasError = true;
      this.isLoading = false;

    }

  }

}


