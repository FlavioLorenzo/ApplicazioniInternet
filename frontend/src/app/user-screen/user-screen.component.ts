import { Component, OnInit } from '@angular/core';
import { Line } from '../Models/Line';

@Component({
  selector: 'app-user-screen',
  templateUrl: './user-screen.component.html',
  styleUrls: ['./user-screen.component.css']
})
export class UserScreenComponent implements OnInit {

  //TODO: FETCH LINES I'M MANAGING AS ADMIN
  myLines: Array<Line> = [
    { id_line: 0, name: "Linea 1" },
    { id_line: 1, name: "Linea 2" },
    { id_line: 2, name: "Linea 3" },
    { id_line: 3, name: "Linea 4" },
    { id_line: 4, name: "Linea 5" }
  ];

  constructor() { }

  ngOnInit() {
  }

}
