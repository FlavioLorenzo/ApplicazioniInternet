import { Component, OnInit, Input } from '@angular/core';
import { Line } from '../Models/Line';

@Component({
  selector: 'app-line-selector',
  templateUrl: './line-selector.component.html',
  styleUrls: ['./line-selector.component.css']
})
export class LineSelectorComponent implements OnInit {

  @Input() checkableLines: Line[];
  @Input() allLines: Line[];
  fixedLines: Line[];

  constructor() { }

  ngOnInit() {
    this.fixedLines = this.allLines.filter(currentLine => {
      const index = this.checkableLines.map(it=>it.id_line).indexOf(currentLine.id_line);
      return index < 0;
    });
  }

  isLineInCommon(lineId){
    return this.allLines.map(it => it.id_line).indexOf(lineId) > 0;
  }

}
