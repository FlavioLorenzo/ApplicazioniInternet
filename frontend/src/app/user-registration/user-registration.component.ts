import { Component, OnInit, Input } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators, FormGroup, FormBuilder} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { Line } from '../Models/Line';


/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

@Component({
  selector: 'app-user-registration',
  templateUrl: './user-registration.component.html',
  styleUrls: ['./user-registration.component.css']
})
export class UserRegistrationComponent implements OnInit {

  @Input() myLines: Array<Line>;

  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  selectFormControl = new FormControl('', [
    Validators.required
  ]);

  matcher = new MyErrorStateMatcher();

  userTypes = [
    { id: "user", name : "Genitore" },
    { id: "companion", name: "Accompagnatore" }
  ]

  selectedValue;

  constructor() {

   }

  ngOnInit() {
    
  }

  onSendRegistration(){
    console.log(`Registering ${this.selectFormControl.value} with mail ${this.emailFormControl.value}`);
  }

}
