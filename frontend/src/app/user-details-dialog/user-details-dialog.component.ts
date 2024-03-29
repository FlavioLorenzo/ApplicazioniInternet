import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ChildrenService } from '../services/children.service';
import { User } from '../Models/User';
import { Line } from '../Models/Line';
import { RegistrationService } from '../services/registration.service';
import { Observable, zip, forkJoin} from 'rxjs';
import { AuthService } from '../services/auth.service';
import {Child} from "../Models/Child";

@Component({
  selector: 'app-user-details-dialog',
  templateUrl: './user-details-dialog.component.html',
  styleUrls: ['./user-details-dialog.component.css']
})
export class UserDetailsDialogComponent implements OnInit {

  user; // TODO: Give a type to this variable

  // Loading state: I'm loading wheter the lines or the children
  isLoading = true;

  // I need to show the update button
  isUserEdited = false;

  // Lines that I'm managing
  myLines: Array<Line>;

  userLines: Array<Line> = [];

  pendingUserLines: Array<Line>;

  // Children belonging to the user
  userChildren: Array<Child> = [];

  constructor(
    private registrationService: RegistrationService,
    private childrenService: ChildrenService,
    private dialogRef: MatDialogRef<UserDetailsDialogComponent>,
    private authService: AuthService,
    @Inject(MAT_DIALOG_DATA) data) {
      this.user = data.user;
      this.myLines = data.myLines;
      console.log(`DIALOG: MYLINES: ${JSON.stringify(this.myLines)}`);
  }

  ngOnInit() {
    console.log(this.user);
    this.childrenService.getChildrenForUser(this.user.id_user).subscribe(children => {
      this.userChildren = children;
    });

    if (this.user.role && this.user.role.rolename !== 'ROLE_USER') {
      this.registrationService.getAdministeredLineOfUser(this.user.id_user).subscribe(
        lines => {
          console.log(`User is managing lines ${lines}`);
          this.userLines = lines;
        }
      );
    }
    this.isLoading = false;
  }

  onLinesChanged(lines) {

    setTimeout(() => {
      this.pendingUserLines = lines;
      this.isUserEdited = !this.arraysEqual(lines, this.userLines);
    }, 0);

  }

  save() {
    //Start array and end array
    //Start - End = Elements that need to be deleted
    //End - Start = Elements that need to be added
    const toDelete = this.userLines.filter(x => !this.pendingUserLines.map(it => it.id_line).includes(x.id_line));
    const toAdd = this.pendingUserLines.filter(x => !this.userLines.map(it => it.id_line).includes(x.id_line));

    console.log(`New lines: ${JSON.stringify(toAdd)}`);
    console.log(`Old lines: ${JSON.stringify(toDelete)}`);

    if(toAdd.length > 0 && this.authService.currentUserValue.role.id_role !== 1){
      //Standard admin trying to delete a certain line
      if(confirm("Confirming the edit you won't be anymore admin of the selected lines. Confirm?")) {
        this.completeEdit(toAdd, toDelete);
      }

    }else{
      //Sys admin or no lines toDelete
      this.completeEdit(toAdd, toDelete);
    }

  }

  completeEdit(toAddLines, toDeleteLines) {
    const addSubscriptions = toAddLines.map(line => this.registrationService.addAdminRoleOfLineToUser(this.user.id_user, line.id_line));
    const removeSubscriptions = toDeleteLines.map(line => this.registrationService.removeAdminRoleOfLineFromUser(this.user.id_user, line.id_line));

    const allSubscription = addSubscriptions.concat(removeSubscriptions);

    forkJoin(allSubscription)
    .subscribe(result => {
        console.log('SUCCESS', JSON.stringify(result));

        this.authService.updateUserRole().subscribe();
        this.dialogRef.close({status: 'success'});
    },
    error => {
      console.log('ERROR', error);
      this.dialogRef.close({status: 'failure'});
    });
  }

  close() {
    this.dialogRef.close();
  }

  //TODO: Move somewhere else
  arraysEqual(_arr1: Array<Line>, _arr2: Array<Line>) {

    if (!Array.isArray(_arr1) || ! Array.isArray(_arr2) || _arr1.length !== _arr2.length)
      return false;

    var arr1 = _arr1.concat().sort();
    var arr2 = _arr2.concat().sort();

    for (var i = 0; i < arr1.length; i++) {

        if (arr1[i].id_line !== arr2[i].id_line)
            return false;

    }

    return true;
  }

}
