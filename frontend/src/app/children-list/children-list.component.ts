import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-children-list',
  templateUrl: './children-list.component.html',
  styleUrls: ['./children-list.component.css']
})
export class ChildrenListComponent implements OnInit {

  childrenList;
  displayedColumns: string[] = ['first_name', 'last_name'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor() { 
  }

  ngOnInit() {

    const children = [
      {first_name: 'Matteo', last_name: 'Bucci'},
      {first_name: 'Matteo', last_name: 'Bucci'},
      {first_name: 'Matteo', last_name: 'Bucci'}
    ]

    this.childrenList = new MatTableDataSource(children);

      setTimeout(() => {
        this.childrenList.sort = this.sort;
      });
  }

}
