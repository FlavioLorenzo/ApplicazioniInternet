import { Component, OnInit } from '@angular/core';
import { Line } from '../Models/Line';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-user-screen',
  templateUrl: './user-screen.component.html',
  styleUrls: ['./user-screen.component.css']
})
export class UserScreenComponent implements OnInit {

  managedLines: Line[];

  constructor(private route: ActivatedRoute) {
     this.managedLines = this.route.snapshot.data.managedLines;
   }

  ngOnInit() {
  }

}
