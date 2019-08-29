import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-attendance-date-line-selection',
  templateUrl: './attendance-date-line-selection.component.html',
  styleUrls: ['./attendance-date-line-selection.component.css']
})
export class AttendanceDateLineSelectionComponent implements OnInit {

  constructor(private router: Router ) { }

  ngOnInit() {}

  onButtonClicked(data) {
    this.router.navigate(['/attendance', 'line', data.line, data.date]);
  }

}
