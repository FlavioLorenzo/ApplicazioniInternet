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

  myLines: Array<Line>;

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

  ngOnInit() {
    this.authService.currentUser.subscribe(currentUser => {
      this.registrationService.getAdministeredLineOfUser(currentUser.id).subscribe(lines => {
        if(lines.length == 0){
          console.log("DEBUG: USING FAKE LINES");
          lines.push({ id_line: 0, name: "Linea 1" },
          { id_line: 1, name: "Linea 2" },
          { id_line: 2, name: "Linea 3" },
          { id_line: 3, name: "Linea 4" },
          { id_line: 4, name: "Linea 5" });
        }
        this.myLines = lines;
      }, error => {
        console.log("DEBUG ON ERROR: USING FAKE LINES");
        const lines = [{ id_line: 0, name: "Linea 1" },
          { id_line: 1, name: "Linea 2" },
          { id_line: 2, name: "Linea 3" },
          { id_line: 3, name: "Linea 4" },
          { id_line: 4, name: "Linea 5" }];

          this.myLines = lines;
      });
    });
  }

  onSendRegistration() {

    if (this.lastNameFormControl.valid && this.firstNameFormControl.valid && this.emailFormControl.valid && this.selectFormControl.valid) {
      const firstName = this.firstNameFormControl.value;
      const lastName = this.lastNameFormControl.value;
      const email = this.emailFormControl.value;
      const role = this.selectFormControl.value;

      this.isLoading = true; // Load indicator

      //const selectedLines = this.lineSelectorComponent.getSelectedLines()

      console.log(`Registering ${firstName} ${lastName} ${email} ${role}`);

      this.registrationService.register(new RegistrationPostBody(email, firstName, lastName, role))
      .subscribe(userResult => {
          console.log(`Registration result: ${JSON.stringify(userResult)}`);
          this.isLoading = false;
          this.clearForm();
          this.snackBar.open("Utente creato con successo");


          this.pendingLinesToMakeAdmin.forEach(lineId => {
            this.registrationService.addAdminRoleOfLineToUser(userResult.id_user, lineId).subscribe(result => {
              console.log(`Line ${lineId} added to user with success`);
            });
          });


      });

    }else{
      console.log('Check the data');
    }

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
