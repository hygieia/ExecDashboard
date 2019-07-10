import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../../../environments/environment';
import { BuildingBlockMetricSummary } from '../domain-models/building-block-metric-summary';
import { HttpHeaders } from '@angular/common/http';

@Injectable()
export class ProductService {
  constructor(private http: HttpClient) {
  }

  getPortfolioProducts(id: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(`${environment.apiUrl}/portfolio/${id}/products`)
      .map((response) => response)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPortfolioExecutives(id: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(`${environment.apiUrl}/portfolio/${id}/executives`)
      .map((response) => response)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPortfolioExecutiveFavs(id: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(`${environment.apiUrl}/portfolio/${id}/executiveFavs`)
      .map((response) => response)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPortfolioExecutivesEids(eids: string[]): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(`${environment.apiUrl}/portfolio/${eids}/allexecutives`)
      .map((response) => response)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPortfolioProduct(portfolioId: string, productId: string): Observable<BuildingBlockMetricSummary> {
    return this.http.get<BuildingBlockMetricSummary>(`${environment.apiUrl}/portfolio/${portfolioId}/products/${productId}`)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getProductComponents(portfolioId: string, productId: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary>(`${environment.apiUrl}/portfolio/${portfolioId}/products/${productId}/components`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getAppCriticalityStatus(appList: string[]): Observable<Map<string, String[]>> {
    return this.http.post(`${environment.apiUrl}/metrics/criticality/status/`, appList)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPreviewCards(): Observable<Map<string, string>> {
    return this.http.get(`${environment.apiUrl}/metrics/previewList`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPreviewSelectedCards(): Observable<Map<string, string>> {
    return this.http.get(`${environment.apiUrl}/metrics/previewSelectList`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getCardsList(): Observable<string[]> {
    return this.http.get(`${environment.apiUrl}/metrics/cardsList`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getProductId(appId: string): Observable<string> {
    return this.http.get(`${environment.apiUrl}/metrics/product/${appId}`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }


  getConfigApps(portfolioId: string): Observable<String[]> {
    return this.http.get(`${environment.apiUrl}/portfolio/configapps/${portfolioId}`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }
}
