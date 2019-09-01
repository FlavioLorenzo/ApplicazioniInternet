import { Injectable } from '@angular/core';
import {environment} from '../../environments/environment';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {AttendanceDisplayComponent} from "../attendance-page/attendance-display/attendance-display.component";

@Injectable({
  providedIn: 'root'
})
export class WebSocketAttendanceService {
  webSocketEndPoint = environment.apiUrl + environment.socketUrl;
  stompClient: any;
  rideId: number;
  attendanceDisplayComponent: AttendanceDisplayComponent;

  constructor() {}

  setup(attendanceDisplayComponent: AttendanceDisplayComponent) {
    this.attendanceDisplayComponent = attendanceDisplayComponent;
  }

  connect(rideId: number) {
    console.log('Initialize WebSocket Connection');
    this.rideId = rideId;
    const ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const that = this;

    // tslint:disable-next-line:only-arrow-functions
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/presence/' + rideId, () => {
        that.attendanceDisplayComponent.getRides();
      });
    }, this.errorCallBack);
  }

  disconnect() {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }
    console.log('Disconnected');
  }

  // on error, schedule a reconnection attempt
  errorCallBack(error) {
    console.log(error);
    console.log('Another attempt will be made after 5 seconds');

    setTimeout(() => {
      this.connect(this.rideId);
    }, 5000);
  }

  /**
   * Send message to the server via web socket
   */
  send() {
    console.log('Calling logout api via web socket');
    this.stompClient.send(
      '/app/update/presence' +
      '/' + this.rideId,
      {},
      {});
  }

}
