import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class TrackService {

    constructor(private http: HttpClient) {
    }

    savePageTracking(view: string, userId: string): Observable<boolean> {
        return this.http.get(`${environment.apiUrl}/users/tracking/${view}/${userId}`);
    }

    savePageTrackForApp(view: string, userId: string, appIds: string[]): Observable<boolean> {
        return this.http.get(`${environment.apiUrl}/users/application/${view}/${userId}/${appIds}`);
    }

    savePageTrackForAppMetric(view: string, userId: string, appIds: string[], metricName: string): Observable<boolean> {
        return this.http.get(`${environment.apiUrl}/users/application/${view}/${userId}/${appIds}/${metricName}`);
    }

    savePageTrackForExec(view: string, userId: string, eids: string[]): Observable<boolean> {
        return this.http.get(`${environment.apiUrl}/users/executive/${view}/${userId}/${eids}`);
    }

    savePageTrackForExecMetric(view: string, userId: string, eids: string[], metricName: string): Observable<boolean> {
        return this.http.get(`${environment.apiUrl}/users/executive/${view}/${userId}/${eids}/${metricName}`);
    }

    getFrequentUsers() {
        return this.http.get(`${environment.apiUrl}/users/recentUsers`)
            .catch(error => {
                console.log(error);
                return Observable.throw(error);
            });
    }

    getFrequentExecutives() {
        return this.http.get(`${environment.apiUrl}/users/recentExecutives`)
            .catch(error => {
                console.log(error);
                return Observable.throw(error);
            });
    }

    getFrequentApplications() {
        return this.http.get(`${environment.apiUrl}/users/recentApplications`)
            .catch(error => {
                console.log(error);
                return Observable.throw(error);
            });
    }

    getFrequentCards() {
        return this.http.get(`${environment.apiUrl}/users/recentCards`)
            .catch(error => {
                console.log(error);
                return Observable.throw(error);
            });
    }

}
