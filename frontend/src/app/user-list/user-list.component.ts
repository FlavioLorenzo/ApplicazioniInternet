import {Component, OnInit, ViewChild} from '@angular/core';
import { UsersService } from '../services/users.service';
import { MatSort, MatDialogConfig, MatDialog } from '@angular/material';
import {MatTableDataSource} from '@angular/material/table';
import { UserDetailsDialogComponent } from '../user-details-dialog/user-details-dialog.component';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  userList;
  displayedColumns: string[] = ['first_name', 'last_name', 'phone', 'email', 'role'];

//@ViewChild(MatSort, {static: true}) sort: MatSort; This doesn't not compile
  @ViewChild(MatSort) sort: MatSort;

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
    dialogConfig.data = user;

    this.dialog.open(UserDetailsDialogComponent, dialogConfig);
}

}
