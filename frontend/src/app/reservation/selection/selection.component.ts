import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-selection',
  templateUrl: './selection.component.html',
  styleUrls: ['./selection.component.css']
})
export class SelectionComponent implements OnInit {
  constructor(private router: Router ) { }

  ngOnInit() {}

  onButtonClicked(data) {
    this.router.navigate(['/reservation', 'line', data.line, data.date]);
  }
}
