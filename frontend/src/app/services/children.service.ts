import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Child } from '../Models/Child';

@Injectable({
  providedIn: 'root'
})
export class ChildrenService {

  constructor(private http: HttpClient) { }

  children: Array<Child> = [
    {user_id: 0, child_id: 1, first_name:"balwant.padwal",last_name:"pune", phone:`33414232542`},
    {user_id: 0, child_id: 2, first_name:"fs.fds",last_name:"fsd", phone:`33414232542`},
    {user_id: 0, child_id: 3, first_name:"fdsfasd.fsdf",last_name:"dsfa", phone:`33414232542`},
  ]

  public getChildrenForUser(userId): Observable<any> {
    return Observable.create(observer => {
      setTimeout(() => {
        observer.next(this.children);
        observer.complete();
      }, 500);
     });
  }

  public registerChild(userId: string, firstName: string, lastName: string, phone: string): Observable<any> {
    return Observable.create(observer => {
      setTimeout(() => {
        const maxIndex = Math.max.apply(null, this.children.map(it =>  it.child_id))
        this.children.push({user_id: 0, child_id: maxIndex + 1, first_name: firstName, last_name: lastName, phone});
        observer.next(0);
        observer.complete();
      }, 500);
    });
  }

  public deleteChild(child: Child){
    return Observable.create(observer => {
      setTimeout(() => {
        const index = this.children.map(it=>it.child_id).indexOf(child.child_id);
        if(index >= 0){
          console.log(`Deleting ${index}`);
          this.children .splice(index, 1);
        }else{
          console.log(`Can't find ${JSON.stringify(child)}`);
        }
        observer.next(this.children);
        observer.complete();
      }, 500);
    });
  }
  
  public getChild(childId: string){
    return Observable.create(observer => {
      setTimeout(() => {
        observer.next({first_name:"balwant.padwal",last_name:"pune"});
        observer.complete();
      }, 500);
     }); 
  }

}
