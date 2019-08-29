import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateUtilsService {

  constructor() { }

  fromPrintFormatToSendFormat(date: string): string {
    const dateElems: Array<string> = date.split('/');

    return dateElems[2] + '-' + dateElems[1] + '-' + dateElems[0];
  }

  getCurrentDate(): string {
    const date: Date = new Date();

    const day = date.getDate();
    let dayString = '';
    if (day < 10) {
      dayString = '0';
    }
    dayString += day.toString();
    const month = date.getMonth();
    let monthString = '';
    if (month < 10) {
      monthString = '0';
    }
    monthString += month.toString();

    return date.getFullYear().toString() + '-' + monthString + '-' + dayString;
  }
}
