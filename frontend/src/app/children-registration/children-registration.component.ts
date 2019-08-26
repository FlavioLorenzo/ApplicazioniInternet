import { Component, OnInit } from '@angular/core';
import { Validators, FormControl } from '@angular/forms';

@Component({
  selector: 'app-children-registration',
  templateUrl: './children-registration.component.html',
  styleUrls: ['./children-registration.component.css']
})
export class ChildrenRegistrationComponent implements OnInit {

  firstNameFormControl = new FormControl('', [
    Validators.required,
  ]);

  lastNameFormControl = new FormControl('', [
    Validators.required,
  ]);

  constructor() {

   }

  ngOnInit() {
    
  }

  onSendRegistration(){
    console.log(`Registering ${this.firstNameFormControl.value} ${this.lastNameFormControl.value}`);
  }

}
