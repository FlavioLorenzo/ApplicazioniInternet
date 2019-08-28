import { Component, OnInit } from '@angular/core';
import { Validators, FormControl } from '@angular/forms';
import { MatSnackBar } from '@angular/material';
import { ChildrenService } from '../services/children.service';

@Component({
  selector: 'app-children-registration',
  templateUrl: './children-registration.component.html',
  styleUrls: ['./children-registration.component.css']
})
export class ChildrenRegistrationComponent implements OnInit {

  isLoading = false;

  firstNameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  phoneFormControl = new FormControl('', []);

  constructor(private snackBar: MatSnackBar, private childrenService: ChildrenService) {

   }

  ngOnInit() {
  }

  onSendRegistration(){

    if(this.lastNameFormControl.valid && this.firstNameFormControl.valid){
      const firstName = this.firstNameFormControl.value;
      const lastName = this.lastNameFormControl.value;
      const phone = this.phoneFormControl.value;

      this.isLoading = true; // Load indicator



      console.log(`Registering ${firstName} ${lastName} ${phone}`);

      this.childrenService.registerChild(1, firstName, lastName, phone).subscribe( result => {
        this.isLoading = false;
        this.clearForm();
        this.snackBar.open("Figlio creato con successo");
      });

    }else{
      console.log('Check the data');
    }


  }

  clearForm() {
    this.lastNameFormControl.setValue('');
    this.firstNameFormControl.setValue('');
    this.phoneFormControl.setValue('');
  }

}
