import { Component, OnInit } from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-shift-definition-date-line-selection',
  templateUrl: './shift-definition-date-line-selection.component.html',
  styleUrls: ['./shift-definition-date-line-selection.component.css']
})
export class ShiftDefinitionDateLineSelectionComponent implements OnInit {
  constructor(private router: Router ) { }

  ngOnInit() {}

  onButtonClicked(data) {
    this.router.navigate(['/shift-definition', 'line', data.line, data.date]);
  }
}
