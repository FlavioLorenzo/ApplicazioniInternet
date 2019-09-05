import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Line } from '../Models/Line';
import { FormControl } from '@angular/forms';

@Component({
  selector: 'app-line-selector',
  templateUrl: './line-selector.component.html',
  styleUrls: ['./line-selector.component.css']
})
export class LineSelectorComponent implements OnInit {

  private _checkableLines; // private property _item
  private _alreadySelectedLines; // private property _item

  get checkableLines(): Line[] { 
    return this._checkableLines;
  }

  @Input()
  set checkableLines(val: Line[]) {
    this._checkableLines = val;
    this.processArguments()
}


get alreadySelectedLines(): Line[] { 
  return this._alreadySelectedLines;
}

@Input()
set alreadySelectedLines(val: Line[]) {
  this._alreadySelectedLines = val;
  this.processArguments()
}

  allLines: Line[];
  @Output() chekedLinesId = new EventEmitter<number[]>();

  linesControl = new FormControl('');


  constructor() { }

  ngOnInit() {
   this.processArguments()
  }

  processArguments(){
    if(this._checkableLines && this._alreadySelectedLines){
      this.allLines = this._checkableLines.concat(this._alreadySelectedLines);
      this.chekedLinesId.next(this._alreadySelectedLines.map(it => it.id_line));
      console.log(`Lines processed`);
    }else{
      console.log(`No lines loaded in the component`);
    }
  }

  onSelectChange(lineIds){
    console.log(`Selected lines: ${JSON.stringify(lineIds)}`);
    this.chekedLinesId.next(lineIds);
  }

  isLineDisabled(lineId){
    const alreadyPresent = this._alreadySelectedLines.map(it => it.id_line).indexOf(lineId) > -1;
    const amIAdmin = this._checkableLines.map(it => it.id_line).indexOf(lineId) > -1;

    return alreadyPresent && !amIAdmin;
  }

  isLineSelected(lineId){
    const alreadyPresent = this._alreadySelectedLines.map(it => it.id_line).indexOf(lineId) > -1;
    return alreadyPresent;
  }

}
