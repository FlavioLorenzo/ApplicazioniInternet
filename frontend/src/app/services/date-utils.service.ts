import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DateUtilsService {

  constructor() { }

  fromPrintFormatToSendFormat(date: string): string {
    let date_elems: Array<string> = date.split('/');

    return date_elems[2] + '-' + date_elems[1] + '-' + date_elems[0];
  }
}
