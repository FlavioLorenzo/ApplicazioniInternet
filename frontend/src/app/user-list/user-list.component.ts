import {Component, OnInit, ViewChild} from '@angular/core';
import { UsersService } from '../services/users.service';
import { MatSort } from '@angular/material';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  userList
  displayedColumns: string[] = ['first_name', 'last_name', 'phone', 'email'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private usersService: UsersService) { 
  }

  ngOnInit() {
    this.usersService.getAllusers().subscribe(users => {
      console.log(`Arrived ${users.length} users`);
      this.userList = new MatTableDataSource(users);
      setTimeout(() => {
        this.userList.sort = this.sort;
      });    });
  }

}
