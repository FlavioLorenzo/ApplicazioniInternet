import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, retry, map, tap} from 'rxjs/operators';
import {environment} from '../../environments/environment';
import {Observable, throwError, BehaviorSubject} from 'rxjs';
import { User } from '../Models/User';
import { AuthService } from './auth.service';

@Injectable()
export class UsersService {


  private fetchedUsers = [];
  private userListSubject: BehaviorSubject<Array<User>>;
  public userList: Observable<Array<User>>;


  constructor(private http: HttpClient, private authService: AuthService) { 
    this.userListSubject = new BehaviorSubject<Array<User>>([]);
    this.userList = this.userListSubject.asObservable();

    this.authService.currentUser.subscribe(
      currentUser => {
        if(currentUser && (currentUser.role.id_role == '1' || currentUser.role.id_role == '2')){
            this.updateUserList(currentUser.id);
        }
      }
    )
  }

  private updateUserList(currentUserId){
    this.getAllusers().subscribe(users => {
      this.fetchedUsers = users;
      this.userListSubject.next(users);
    });
  }

  public getAllusers(): Observable<any> {
    return this.http.get<any>(
      environment.apiUrl +
      environment.usersUrl)
      .pipe(
        tap(users => this.updateUserList(users)),
        retry(3),
        catchError(err => {
          console.error(err.message);
          console.log('Error is handled');
          return throwError('Error thrown from catchError');
        })
     );
  }

}
