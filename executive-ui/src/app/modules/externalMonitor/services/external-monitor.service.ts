import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { environment } from '../../../../environments/environment';
import { ExternalSystemMonitor } from '../../../../app/modules/shared/domain-models/externalSystemMonitor';
import { CollectorUpdatedStatus } from '../../../../app/modules/shared/domain-models/collectorUpdatedStatus';

@Injectable()
export class ExternalMonitorService {

	private userUri = `${environment.apiUrl}`;

	constructor(private http: HttpClient) {}
	
	getCollectorUpdatedTimeStamps(): Observable<CollectorUpdatedStatus[]> {
	return this.http.get<CollectorUpdatedStatus[]>(`${this.userUri}/metrics/getCollectorTimeStamps/other`)
      .map((response) => response)
      .catch((error) =>  { console.log(error); return Observable.throw(error); });
	}

	getMetricCollectorUpdatedTimeStamps(): Observable<CollectorUpdatedStatus[]> {
	return this.http.get<CollectorUpdatedStatus[]>(`${this.userUri}/metrics/getCollectorTimeStamps/MetricsProcessor`)
      .map((response) => response)
      .catch((error) =>  { console.log(error); return Observable.throw(error); });
	}

	
}
