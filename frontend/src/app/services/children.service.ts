import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ChildrenService {

  constructor(private http: HttpClient) { }

  public getChildForUser(userId): Observable<any> {
    return Observable.create(observer => {
      setTimeout(() => {
        let children = [
          {first_name:"balwant.padwal",last_name:"pune"},
          {first_name:"test",last_name:"mumbai"}]
        observer.next(children);
        observer.complete();
      }, 500);
     });
  }

  public registerChild(userId: string, firstName: string, lastName: string): Observable<any> {
    return Observable.create(observer => {
      setTimeout(() => {
        observer.next(0);
        observer.complete();
      }, 500);
    });
  }

  public deleteChild(userId: string, childId: string){
    return Observable.create(observer => {
      setTimeout(() => {
        observer.next(0);
        observer.complete();
      }, 500);
    });
  }

}
