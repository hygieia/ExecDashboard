import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { environment } from '../../../../environments/environment';
import { ExecutiveStatus } from '../../../../app/modules/shared/domain-models/executiveStatus';

@Injectable()
export class UserTrackService {

	private userUri = `${environment.apiUrl}/users`;

	constructor(private http: HttpClient) { }

	getHitsInfo() {
		return this.http.get(`${this.userUri}/hitsInfo`)
			.map((response) => response)
			.catch((error) => { console.log(error); return null; });
	}

	getHitsForTheDay() {
		return this.http.get(`${this.userUri}/hitsInfoForTheDay`)
			.map((response) => response)
			.catch((error) => { console.log(error); return null; });
	}

	getExecutivesListAccessed(): Observable<ExecutiveStatus[]> {
		return this.http.get<ExecutiveStatus[]>(`${this.userUri}/executivesList`)
			.map((response) => response)
			.catch((error) => { console.log(error); return Observable.throw(error); });
	}


}
