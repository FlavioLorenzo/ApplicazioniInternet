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

    return this.dateToSendString(date);
  }

  getDateAfterNDays(n: number): string {
    const date = new Date();
    date.setDate(date.getDate() + n);

    return this.dateToSendString(date);
  }

  dateToSendString(date: Date) {
    const day = date.getDate();
    let dayString = '';
    if (day < 10) {
      dayString = '0';
    }
    dayString += day.toString();
    const month = date.getMonth() + 1;
    let monthString = '';
    if (month < 10) {
      monthString = '0';
    }
    monthString += month.toString();

    return date.getFullYear().toString() + '-' + monthString + '-' + dayString;
  }

  sendStringToDate(dateString: string): Date {
    return new Date(dateString);
  }
}
