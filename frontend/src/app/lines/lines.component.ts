import {Component, Input, OnInit} from '@angular/core';
import {LinesService} from '../services/lines.service';
import {AttendanceComponent} from '../attendance/attendance.component';

@Component({
  selector: 'app-lines',
  templateUrl: './lines.component.html',
  styleUrls: ['./lines.component.css']
})
export class LinesComponent implements OnInit {

  @Input() attendance: AttendanceComponent;

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

  selectLine(idLine) {
    console.log("This is my line ");
    console.log(idLine);
    this.attendance.changeLine(idLine);
  }
}
