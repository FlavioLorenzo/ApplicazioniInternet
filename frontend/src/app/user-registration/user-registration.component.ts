import { Component, OnInit, Input, ViewChild } from '@angular/core';
import {FormControl, FormGroupDirective, NgForm, Validators, FormGroup, FormBuilder} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { Line } from '../Models/Line';
import { MatSnackBar } from '@angular/material';
import { LineSelectorComponent } from '../line-selector/line-selector.component';
import { RegistrationService, RegistrationPostBody } from '../services/registration.service';
import { AuthService } from '../services/auth.service';


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

  @Input() managedLines: Array<Line>;

  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  selectFormControl = new FormControl('', [Validators.required]);
  firstNameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  matcher = new MyErrorStateMatcher();
  pendingLinesToMakeAdmin = [];

  isLoading = false; // Load indicator

  userTypes = [
    { id: 4, name : "Genitore" },
    { id: 3, name: "Accompagnatore" }
  ]


  @ViewChild(LineSelectorComponent, {static: false})
  private lineSelectorComponent: LineSelectorComponent;

  constructor(private snackBar: MatSnackBar, private registrationService: RegistrationService, private authService: AuthService) {}

  ngOnInit() { }

  onSendRegistration() {


    if (this.lastNameFormControl.valid && this.firstNameFormControl.valid && this.emailFormControl.valid && this.selectFormControl.valid) {
      const firstName = this.firstNameFormControl.value;
      const lastName = this.lastNameFormControl.value;
      const email = this.emailFormControl.value;
      const role = this.selectFormControl.value;

      if(role === 3 && this.pendingLinesToMakeAdmin.length > 0 && this.authService.currentUserValue.role.id_role !== '1'){
        if(confirm("Confirming the registration you won't be anymore admin of the selected lines. Confirm?")) {
          this.completeRegistration(firstName, lastName, email, role);
        }
      }else{
        this.completeRegistration(firstName, lastName, email, role);
      }
    }else{
      console.log('Check the data');
    }
  }

  completeRegistration(firstName, lastName, email, role){
    
    this.isLoading = true; // Load indicator

    console.log(`Registering ${firstName} ${lastName} ${email} ${role}`);

    this.registrationService.register(new RegistrationPostBody(email, firstName, lastName, role))
    .subscribe(userResult => {
        console.log(`Registration result: ${JSON.stringify(userResult)}`);
        this.isLoading = false;
        this.clearForm();
        this.snackBar.open("User created successfully!");
        this.pendingLinesToMakeAdmin.forEach(lineId => {
          this.registrationService.addAdminRoleOfLineToUser(userResult.id_user, lineId).subscribe(result => {
            console.log(`Line ${lineId} added to user with success`);
          });
        });


    },
    error => {
      this.snackBar.open("Sorry! Something went wrong. Try again later.");
      this.isLoading = false;
    });

 
}

  onLinesChanged(lines){
    this.pendingLinesToMakeAdmin = lines;
  }

  clearForm() {
    this.lastNameFormControl.setValue('');
    this.firstNameFormControl.setValue('');
    this.emailFormControl.setValue('');
    this.selectFormControl.setValue('');
    this.emailFormControl = new FormControl('', [Validators.required, Validators.email]);
    this.selectFormControl = new FormControl('', [Validators.required]);
    this.firstNameFormControl = new FormControl('', [Validators.required]);
    this.lastNameFormControl = new FormControl('', [Validators.required]);
  }

}
