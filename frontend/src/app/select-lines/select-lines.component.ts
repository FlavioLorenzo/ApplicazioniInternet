import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {LinesService} from '../services/lines.service';

@Component({
  selector: 'app-select-lines',
  templateUrl: './select-lines.component.html',
  styleUrls: ['./select-lines.component.css']
})
export class SelectLinesComponent implements OnInit {

  @Output() lineSelected = new EventEmitter<number>();
  lines = [];

  constructor(private linesService: LinesService) {}

  ngOnInit() {
    this.linesService.getAllLines()
      .subscribe(
        (data) => {
          this.lines = data;
        },
        (error) => {console.error(error); },
        () => {console.log('Lines retrieved'); }
      );
  }

  onSelectLine(idLine) {
    this.lineSelected.emit(idLine);
  }

}
