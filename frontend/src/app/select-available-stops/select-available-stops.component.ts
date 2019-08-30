import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {RideService} from '../services/ride.service';

@Component({
  selector: 'app-select-available-stops',
  templateUrl: './select-available-stops.component.html',
  styleUrls: ['./select-available-stops.component.css']
})
export class SelectAvailableStopsComponent implements OnInit {
  @Input() rideId: number;
  @Output() stopSelected = new EventEmitter<number>();
  stops = [];

  constructor(private rideService: RideService) {}

  ngOnInit() {
    this.rideService.getAvailableStops(this.rideId)
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
