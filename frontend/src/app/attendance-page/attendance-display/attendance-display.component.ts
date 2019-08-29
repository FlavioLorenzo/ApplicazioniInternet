import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {DateUtilsService} from '../../services/date-utils.service';

@Component({
  selector: 'app-attendance-display',
  templateUrl: './attendance-display.component.html',
  styleUrls: ['./attendance-display.component.css']
})
export class AttendanceDisplayComponent implements OnInit {
  lineId: number;
  lineName = 'Test';
  date: string;

  constructor(private route: ActivatedRoute, private router: Router, private dateService: DateUtilsService) { }

  ngOnInit() {
    this.route.params.subscribe((params: Params) => {
      this.lineId = params.id;
      this.date = params.from;
    });

    if (this.lineId == null) {
      this.defaultSetup();
    }
  }

  defaultSetup() {
    this.date = this.dateService.getCurrentDate();
    this.lineId = 1; // ToDo: After setting up the administration of the lines, this part won't be needed anymore
  }

  onBackClick() {
    this.router.navigate(['attendance', 'selection']);
  }
}
