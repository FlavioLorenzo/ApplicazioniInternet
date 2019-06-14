import { Component, OnInit } from '@angular/core';
import {ReservationsService} from '../services/reservations.service';
import {LinesService} from '../services/lines.service';

@Component({
  selector: 'app-lines',
  templateUrl: './lines.component.html',
  styleUrls: ['./lines.component.css']
})
export class LinesComponent implements OnInit {

  lines = [];

  constructor(private linesService: LinesService) {}


  ngOnInit() {
    this.linesService.getAllLines()
      .subscribe(
        (data) => {
          console.log('Lines arrived');
          console.log(JSON.stringify(data));
          this.lines = data;
        },
        (error) => {console.error(error)},
        () => {console.log('Lines retrieved')}
      );
  }

  selectLine(line){
    console.log(JSON.stringify(line));
  }


}
