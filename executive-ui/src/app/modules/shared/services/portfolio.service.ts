import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { Portfolio } from '../domain-models/portfolio';
import { BusinessUnits } from '../domain-models/businessUnits';
import { environment } from '../../../../environments/environment';
import { Clarity } from '../domain-models/clarity';

@Injectable()
export class PortfolioService {
  private executiveUri = `${environment.apiUrl}/portfolio`;

  constructor(private http: HttpClient) { }

  getPortfolios(): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(this.executiveUri)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  getPortfolio(id: string): Observable<Portfolio> {
    return this.http.get<Portfolio>(`${this.executiveUri}/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  getBusinessUnits(): Observable<BusinessUnits[]> {
    return this.http.get<BusinessUnits[]>(`${this.executiveUri}/businessUnits`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  getBusinessUnitForExecutive(eid: string): Observable<BusinessUnits[]> {
    return this.http.get<BusinessUnits[]>(`${this.executiveUri}/businessUnits/${eid}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  getPortfoliosExecutive(id: string): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(`${this.executiveUri}/executives/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  getExecutivesList(id: string) {
    return this.http.get(`${this.executiveUri}/executivesLists/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getExecutivesListAll(id: string) {
    return this.http.get(`${this.executiveUri}/executivesListsAll/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getApplicationListAllForExec(id: string) {
    return this.http.get(`${this.executiveUri}/applicationListForExec/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getAllApplicationList() {
    return this.http.get(`${this.executiveUri}/getAllApplicationList`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getApplicationListAllForLob(lob: string) {
    return this.http.get(`${this.executiveUri}/applicationList/${lob}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getApplicationListAllForExecWithBunit(bunit: string, id: string) {
    return this.http.get(`${this.executiveUri}/applicationListForExec/${bunit}/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getReportings(id: string) {
    return this.http.get(`${this.executiveUri}/reportings/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getTimePeriod(): Observable<Map<string, string>> {
    return this.http.get(`${this.executiveUri}/timePeriods`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getClarityListMap(id: string): Observable<Clarity> {
    return this.http.get(`${environment.apiUrl}/metrics/program/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }
  getFavourite(id: string): Observable<Object> {
    return this.http.get(`${this.executiveUri}/getFavsOfEid/${id}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return null; });
  }

  getExecutivesListForBunit(id: string, lob: string): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(`${this.executiveUri}/executivesHierarchy/${id}/${lob}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); });
  }

  setFav(eid: string, favEid: Array<String>): Observable<boolean> {
    return this.http.get<boolean>(`${this.executiveUri}/setFav/${eid}/${favEid}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); })
  }

  removeFav(eid: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.executiveUri}/removeFav/${eid}`)
      .map((response) => response)
      .catch((error) => { console.log(error); return Observable.throw(error); })
  }

}
