import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort, MatTableDataSource } from '@angular/material';
import { ChildrenService } from '../services/children.service';
import { Child } from '../Models/Child';

@Component({
  selector: 'app-children-list',
  templateUrl: './children-list.component.html',
  styleUrls: ['./children-list.component.css']
})
export class ChildrenListComponent implements OnInit {

  childrenList: MatTableDataSource<Child> = null;
  displayedColumns: string[] = ['first_name', 'last_name', 'phone', 'delete'];

  @ViewChild(MatSort, {static: true}) sort: MatSort;

  constructor(private childrenService: ChildrenService) { 
  }

  ngOnInit() { 
    this.childrenService.getChildrenForUser(0).subscribe(childrenList => {
      this.childrenList = new MatTableDataSource(childrenList);
      setTimeout(() => {
        this.childrenList.sort = this.sort;
      });
    });
  }

  onDeleteChild(child: Child){
    console.log(`Pressed deleteChild with ${JSON.stringify(child)}`);
    this.childrenService.deleteChild(child).subscribe(childrenList=>{
      this.childrenList = new MatTableDataSource(childrenList);
      setTimeout(() => {
        this.childrenList.sort = this.sort;
      });
    });
  }

}
