import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ChildrenService } from '../services/children.service';
import { User } from '../Models/User';
import { Line } from '../Models/Line';

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
  myLines: Array<Line> = [
    {id_line: 0, name: 'linea 1'},
    {id_line: 1, name: 'linea 2'},
    {id_line: 2, name: 'linea 3'},
    {id_line: 3, name: 'linea 4'},
    {id_line: 4, name: 'linea 5'}
  ];

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
      this.isLoading = false;
    }

  }

  save() {
    this.dialogRef.close();
  }

  close() {
    this.dialogRef.close();
  }

}
