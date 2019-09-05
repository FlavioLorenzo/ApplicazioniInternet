import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ChildrenService } from '../services/children.service';
import { User } from '../Models/User';
import { Line } from '../Models/Line';
import { RegistrationService } from '../services/registration.service';

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

  userLines: Array<Line> = [
    {id_line: 3, name: 'linea 4'},
    {id_line: 4, name: 'linea 5'},
    {id_line: 5, name: 'linea 6'},
    {id_line: 6, name: 'linea 7'},
    {id_line: 7, name: 'linea 8'}
  ];

  // Children beloning to the user
  userChildren;

  constructor(
    private registrationService: RegistrationService,
    private childrenService: ChildrenService,
    private dialogRef: MatDialogRef<UserDetailsDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data) {
      this.user = data.user;
      this.myLines = data.myLines;
  }

  ngOnInit() {
    if (this.user.role && this.user.role.rolename === 'ROLE_USER') {
      this.childrenService.getChildrenForUser(this.user.userId).subscribe(children => {
        this.userChildren = children;
        this.isLoading = false;
      });
    } else {

      this.registrationService.getAdministeredLineOfUser(this.user.id_user).subscribe(
        lines => {
          console.log(`User is managing lines ${lines}`);
          this.userLines = lines;
        }
      );


      this.isLoading = false;
    }

  }

  onLinesChanged(lines){

    setTimeout(() => {
      this.isUserEdited = !this.arraysEqual(lines, this.userLines.map(it => it.id_line));
    }, 0);

  }

  save() {
    this.dialogRef.close();
  }

  close() {
    this.dialogRef.close();
  }

  //TODO: Move somewhere else
  arraysEqual(_arr1, _arr2) {

    if (!Array.isArray(_arr1) || ! Array.isArray(_arr2) || _arr1.length !== _arr2.length)
      return false;

    var arr1 = _arr1.concat().sort();
    var arr2 = _arr2.concat().sort();

    for (var i = 0; i < arr1.length; i++) {

        if (arr1[i] !== arr2[i])
            return false;

    }

    return true;
  }

}
