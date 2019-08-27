import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {StopService} from '../services/stop.service';

@Component({
  selector: 'app-select-available-stops',
  templateUrl: './select-available-stops.component.html',
  styleUrls: ['./select-available-stops.component.css']
})
export class SelectAvailableStopsComponent implements OnInit {
  @Input() rideId: number;
  @Output() stopSelected = new EventEmitter<number>();
  stops = [];

  constructor(private stopService: StopService) {}

  ngOnInit() {
    this.stopService.getAvailableStops(this.rideId)
      .subscribe(
        (data) => { this.stops = data; },
        (error) => {console.error(error); },
        () => {console.log('Stops retrieved'); }
      );
  }

  onSelectStop(idStop) {
    this.stopSelected.emit(idStop);
  }
}
