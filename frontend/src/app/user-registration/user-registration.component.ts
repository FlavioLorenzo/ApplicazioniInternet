import { Component, OnInit, Input, ViewChild } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators, FormGroup, FormBuilder} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { Line } from '../Models/Line';
import { MatSnackBar } from '@angular/material';
import { LineSelectorComponent } from '../line-selector/line-selector.component';


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

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  selectFormControl = new FormControl('', [Validators.required]);
  firstNameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  matcher = new MyErrorStateMatcher();

  isLoading = false; // Load indicator


  userTypes = [
    { id: "user", name : "Genitore" },
    { id: "companion", name: "Accompagnatore" }
  ]

  selectedValue;

  @ViewChild(LineSelectorComponent, {static: false})
  private lineSelectorComponent: LineSelectorComponent;

  constructor(private snackBar: MatSnackBar) {

   }

  ngOnInit() {

  }

  onSendRegistration() {

    if(this.lastNameFormControl.valid && this.firstNameFormControl.valid && this.emailFormControl.valid && this.selectFormControl.valid){
      const firstName = this.firstNameFormControl.value;
      const lastName = this.lastNameFormControl.value;
      const email = this.emailFormControl.value;
      const role = this.selectFormControl.value;

      this.isLoading = true; // Load indicator

      //const selectedLines = this.lineSelectorComponent.getSelectedLines()

      console.log(`Registering ${firstName} ${lastName} ${email} ${role}`);

      setTimeout(()=>{
        this.isLoading = false;
        this.clearForm();
        this.snackBar.open("Utente creato con successo");
      }, 1000);
    }else{
      console.log('Check the data');
    }

  }


  clearForm() {
    this.lastNameFormControl.setValue('');
    this.firstNameFormControl.setValue('');
    this.emailFormControl.setValue('');
    this.selectFormControl.setValue('');
  }

}
