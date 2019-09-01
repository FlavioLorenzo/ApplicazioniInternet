import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ChildrenService} from '../services/children.service';
import {AuthService} from '../services/auth.service';
import {Child} from "../Models/Child";

@Component({
  selector: 'app-select-child',
  templateUrl: './select-child.component.html',
  styleUrls: ['./select-child.component.css']
})
export class SelectChildComponent implements OnInit {

  @Output() childSelected = new EventEmitter<number>();
  children: Array<Child> = [];

  constructor(private childrenService: ChildrenService, private auth: AuthService) {}

  ngOnInit() {
    this.childrenService.getChildrenForUser(this.auth.currentUserValue.id)
      .subscribe(
        (data) => {
          console.log(data);
          this.children = data;
        },
        (error) => {console.error(error); },
        () => {console.log('Lines retrieved'); }
      );
  }

  onSelectChild(idChild) {
    this.childSelected.emit(idChild);
  }
}
