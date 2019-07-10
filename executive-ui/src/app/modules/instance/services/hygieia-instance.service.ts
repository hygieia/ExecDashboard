import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { SoftwareVersion } from '../../shared/domain-models/softwareVersion';
import { environment } from '../../../../environments/environment';


@Injectable()
export class HygieiaInstanceService {
  private executiveUri = `${environment.apiUrl}`;
  constructor(private http: HttpClient) { }

  public getPatchBusinessUnitsData(bunit: string): Observable<SoftwareVersion> {
		return this.http.get<SoftwareVersion>(`${this.executiveUri}/instance/getPatchVersions/${bunit}`)
		.map((response) => response)
		.catch((error) =>  { console.log(error); return Observable.throw(error); });
	}
}
