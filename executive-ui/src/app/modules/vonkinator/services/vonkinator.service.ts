import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { Vonkinator } from '../../shared/domain-models/vonkinator';
import { environment } from '../../../../environments/environment';


@Injectable()
export class VonkinatorService {
  private executiveUri = `${environment.apiUrl}`;
  constructor(private http: HttpClient) { }

  public getAllVonkinatorData(): Observable<Vonkinator[]> {
    return this.http.get<Vonkinator[]>(`${this.executiveUri}/vonkinator/getAll`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  public getAllNonITVonkinatorData(): Observable<Vonkinator[]> {
    return this.http.get<Vonkinator[]>(`${this.executiveUri}/vonkinator/getAllNonIT`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

}
