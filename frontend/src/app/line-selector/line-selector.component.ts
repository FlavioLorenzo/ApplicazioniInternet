import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Line } from '../Models/Line';
import { FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-line-selector',
  templateUrl: './line-selector.component.html',
  styleUrls: ['./line-selector.component.css']
})
export class LineSelectorComponent implements OnInit {

/* ----- Input ------- */

  //Lines of which I'm admin
  private _checkableLines = []; // private property _item

  //Lines of user is admin
  private _alreadySelectedLines = []; // private property _item

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
    this.processArguments();
  }

  @Input() disableAll = false

  /* -------------------------- */

  allLines: Line[];

  @Output() checkedLines = new EventEmitter<Line[]>();

  linesControl = new FormControl('');

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.processArguments();
  }

  processArguments(){

    console.log(this.authService.currentUserValue.role.id_role);
    console.log(this.authService.currentUserValue.role.id_role === 1);


      this.allLines = this.getUnique(this._checkableLines.concat(this._alreadySelectedLines), 'id_line');

      setTimeout(()=>{
        this.setPreselectedLines(this.alreadySelectedLines);
      }, 0);

  }

  onSelectChange(lines){
    console.log(`Lines selected changed: ${JSON.stringify(lines)}`);
    this.checkedLines.next(lines);
  }

  isLineDisabled(line: Line){
    const alreadyPresent = this._alreadySelectedLines.map(it => it.id_line).indexOf(line.id_line) > -1;
    const amIAdmin = this._checkableLines.map(it => it.id_line).indexOf(line.id_line) > -1;
    return ((alreadyPresent && !amIAdmin && this.authService.currentUserValue.role.id_role !== '1') || this.disableAll);
  }

  setPreselectedLines(lines: Array<Line>){

    const selectedLines = this.allLines.filter(line => lines.map(it => it.id_line).indexOf(line.id_line) >= 0);

    console.log(`Setting selected lines as: ${JSON.stringify(lines)}`);
    this.linesControl.setValue(selectedLines);
  }

  getUnique(arr,comp){

    return arr.map(e => e[comp]).map((e, i, final) =>final.indexOf(e) === i && i).filter((e)=> arr[e]).map(e=>arr[e]);

  }

}
