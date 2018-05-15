import {Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { Portfolio } from '../domain-models/portfolio';
import {environment} from '../../../../environments/environment';

@Injectable()
export class PortfolioService {
  private executiveUri = `${environment.apiUrl}/portfolio`;

  constructor(private http: HttpClient) { }

  getPortfolios(): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(this.executiveUri)
      .map((response) => response)
      .catch((error) =>  { console.log(error); return Observable.throw(error); });
  }

  getPortfolio(name: string, lob: string): Observable<Portfolio> {
    return this.http.get<Portfolio>(`${this.executiveUri}/${name}/${lob}`)
      .map((response) => response)
      .catch((error) =>  { console.log(error); return Observable.throw(error); });
  }
}
