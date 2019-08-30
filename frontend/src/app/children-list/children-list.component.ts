import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';
import { ChildrenService } from '../services/children.service';
import { Child } from '../Models/Child';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-children-list',
  templateUrl: './children-list.component.html',
  styleUrls: ['./children-list.component.css']
})
export class ChildrenListComponent implements OnInit {

  childrenList: MatTableDataSource<Child> = null;
  displayedColumns: string[] = ['first_name', 'last_name', 'phone', 'delete'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private childrenService: ChildrenService, private authService: AuthService) {}

  ngOnInit() {

    // Query the user children
    this.childrenService.getChildrenForUser(this.authService.currentUserValue.id)
      .subscribe(childrenList => this.setDataSource(childrenList));

  }

  onDeleteChild(child: Child) {

    // Browser confirm dialog
    if (confirm(`Confirm to delete child ${child.first_name} + ${child.last_name}? This operation can't be undone.`)) {
     // Delete child from service
      this.childrenService.deleteChild(child.id_child)
      .subscribe(
        () => {
          //TODO: REFRESHARE LA LISTA
          console.log(`Cancellazione effettuata con successo`);
        }
      );
    }

  }

  private setDataSource(childrenList) {

    // Set the datasource with the new data
    this.childrenList = new MatTableDataSource(childrenList);
    // Set the table sort
    setTimeout(() => { this.childrenList.sort = this.sort; });

  }

}
