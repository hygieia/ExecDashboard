import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { Application } from '../domain-models/application';
import { environment } from '../../../../environments/environment';

@Injectable()
export class ApplicationService {
  private executiveUri = `${environment.apiUrl}/applications`;

  constructor(private http: HttpClient) { }

  getApplications(): Observable<Application[]> {
    return this.http.get<Application[]>(this.executiveUri)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }
}
