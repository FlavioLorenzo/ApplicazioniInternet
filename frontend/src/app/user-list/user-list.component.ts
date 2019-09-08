import {Component, OnInit, ViewChild, Input} from '@angular/core';
import { UsersService } from '../services/users.service';
import { MatDialogConfig, MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import { UserDetailsDialogComponent } from '../user-details-dialog/user-details-dialog.component';
import { Line } from '../Models/Line';
import { AuthService } from '../services/auth.service';
import { RegistrationService } from '../services/registration.service';
import { MatSnackBar, MatPaginator } from '@angular/material';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  userList;
  displayedColumns: string[] = ['first_name', 'last_name', 'phone', 'email', 'role'];
  @Input() managedLines: Array<Line>;

  @ViewChild(MatSort, {static: true}) sort: MatSort;
  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator;


  constructor(private usersService: UsersService, private dialog: MatDialog, private authService: AuthService, private registrationService: RegistrationService, private snackBar: MatSnackBar) { 
  }

  ngOnInit() {

    this.usersService.userList.subscribe(users => {
 
      this.userList = new MatTableDataSource(users);

      console.log(JSON.stringify(users));

      //How the **** I'm supposed to know that this line has to be called inside a timeout?
      setTimeout(() => {
        this.userList.sort = this.sort;
        this.userList.paginator = this.paginator;

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
      myLines: this.managedLines
    }
    dialogConfig.minWidth = '400px';

    const dialogRef = this.dialog.open(UserDetailsDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(result => {

      if (result  && result.status === 'success'){
        this.snackBar.open("Utente aggiornato con successo");
        this.usersService.updateUserList();
      } else if (result  && result.status === 'failure'){
        this.snackBar.open("Errore nell'aggiornamento dell'utente");
      }

    });

}

}
