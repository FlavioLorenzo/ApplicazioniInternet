import { Component, OnInit } from '@angular/core';
import {AuthService} from '../services/auth.service';
import {User} from '../Models/User';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CompleteUserPostBody} from '../services/registration.service';
import {first} from 'rxjs/operators';

@Component({
  selector: 'app-password-reset',
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css'],
  providers: [AuthService]
})
export class PasswordResetComponent implements OnInit {

  form: FormGroup;
  submitted = false;
  countErrors = 0;

  resetComplete = false;
  hasError = false;
  isLoading = true;

  token: string;
  displayName: string;

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private route: ActivatedRoute,
              private router: Router) {
    this.form = this.fb.group({
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(32)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(32)]]
    });
  }

  onReset() {
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
      node.appendChild(document.createTextNode('Please check if password is more than 8 characters long.'));
      mexBox.appendChild(node);
      this.countErrors++;
      return;
    }

    const val = this.form.value;

    if (val.password !== val.confirmPassword) {
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


    if (val.password && val.confirmPassword) {
      // tslint:disable-next-line:max-line-length
      this.authService.resetPassword(this.token, val.password, val.confirmPassword)
        .subscribe(
          (data) => {
            console.log(data);
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

  ngOnInit() {
    console.log('init');
    this.token = this.route.snapshot.paramMap.get('token');

    if (this.token) {
      this.authService.getResetTokenInfo(this.token)
        .subscribe(result => {
          const user: User = result;
          this.displayName = `${user.first_name} ${user.last_name}`;
          this.isLoading = false;
          this.hasError = false;
        }, () => {
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
