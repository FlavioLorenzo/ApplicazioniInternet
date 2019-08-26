import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { UsersService } from '../services/users.service';

@Component({
  selector: 'app-user-details-dialog',
  templateUrl: './user-details-dialog.component.html',
  styleUrls: ['./user-details-dialog.component.css']
})
export class UserDetailsDialogComponent implements OnInit {

  user
  userChildren
  isUserEdited = false

  constructor(private usersService: UsersService, private dialogRef: MatDialogRef<UserDetailsDialogComponent>, @Inject(MAT_DIALOG_DATA) data) { 
    this.user = data
  }

  ngOnInit() {
    this.usersService.getChildForUser(this.user.id_user).subscribe(children => {
      this.userChildren = children;
    });
  }


  save() {
    this.dialogRef.close();
  } 

  close() {
    this.dialogRef.close();
  }

}
