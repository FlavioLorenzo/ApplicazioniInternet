import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ChildrenService } from '../services/children.service';

@Component({
  selector: 'app-user-details-dialog',
  templateUrl: './user-details-dialog.component.html',
  styleUrls: ['./user-details-dialog.component.css']
})
export class UserDetailsDialogComponent implements OnInit {

  user
  userChildren
  isLoading = true
  isUserEdited = false

  constructor(private childrenService: ChildrenService, private dialogRef: MatDialogRef<UserDetailsDialogComponent>, @Inject(MAT_DIALOG_DATA) data) { 
    this.user = data
  }

  ngOnInit() {
    this.childrenService.getChildForUser(this.user.id_user).subscribe(children => {
      this.userChildren = children;
      this.isLoading = false
    });
  }


  save() {
    this.dialogRef.close();
  } 

  close() {
    this.dialogRef.close();
  }

}
