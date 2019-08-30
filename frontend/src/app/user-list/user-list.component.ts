import {Component, OnInit, ViewChild, Input} from '@angular/core';
import { UsersService } from '../services/users.service';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { UserDetailsDialogComponent } from '../user-details-dialog/user-details-dialog.component';
import { Line } from '../Models/Line';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  userList;
  displayedColumns: string[] = ['first_name', 'last_name', 'phone', 'email', 'role'];
  @Input() myLines: Array<Line>;

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private usersService: UsersService, private dialog: MatDialog) { 
  }

  ngOnInit() {

    this.usersService.getAllusers().subscribe(users => {
 
      this.userList = new MatTableDataSource(users);

      console.log(JSON.stringify(users));

      //How the **** I'm supposed to know that this line has to be called inside a timeout?
      setTimeout(() => {
        this.userList.sort = this.sort;
      });
      
    });
  }

  onUserPressed(user){
    this.openDialog(user);
  }

  openDialog(user) {

    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      user,
      myLines: this.myLines
    }
    dialogConfig.minWidth = '400px';

    this.dialog.open(UserDetailsDialogComponent, dialogConfig);
}

}
