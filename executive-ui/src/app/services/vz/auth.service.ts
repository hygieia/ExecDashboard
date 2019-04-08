import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

@Injectable()
export class AuthService {

	public authData = new BehaviorSubject('defaultMessage');
	public authEid: string;
	currentAuthData = this.authData.asObservable();
	constructor(private http: HttpClient) {

	}

	setAuth(data) {
		this.authData.next(data);
		this.http.post(`${environment.apiUrl}/auth/resgisterUser`, data)
			.subscribe(response => console.log(response));
	}

	setAuthEid(data) {
		this.authEid = data;
	}

	getAuthEid() {
		return this.authEid;
	}

	getPortfolioId(eId: string): Observable<string> {
		return this.http.get(`${environment.apiUrl}/auth/getPortfolioId/${eId}`, eId);

	}

	isAdmin(eId: string){
        return this.http.get(`${environment.apiUrl}/auth/checkisAdmin/${eId}`, eId);
	}

}