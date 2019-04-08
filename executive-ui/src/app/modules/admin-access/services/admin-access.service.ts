import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { UserDetails } from '../../shared/domain-models/userDetails';

@Injectable()
export class AdminAccessService {
  private executiveUri = `${environment.apiUrl}`;
  constructor(private http: HttpClient) { }


  public makeAdmin(appId:string,username: string): Observable<boolean> {
		return this.http.get<boolean>(`${this.executiveUri}/defaulthygieia/${appId}/admin/${username}`)
		.map((response) => response)
		.catch((error) =>  { console.log(error); return Observable.throw(error); });
  }
  public getUsersforAdmin(appId:string): Observable<UserDetails[]> {
    return this.http.get<UserDetails[]>(`${this.executiveUri}/defaulthygieia/admin/${appId}`)
		.map(response => response)
		.catch(error  =>  { console.log(error); return Observable.throw(error); });
  }

}
