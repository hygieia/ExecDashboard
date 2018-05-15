import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../../../../environments/environment';
import {BuildingBlockMetricSummary} from '../domain-models/building-block-metric-summary';

@Injectable()
export class ProductService {
  constructor(private http: HttpClient) {
  }

  getPortfolioProducts(businessOwnerName: string, businessOwnerLob: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(`${environment.apiUrl}/portfolio/${businessOwnerName}/${businessOwnerLob}/products`)
      .map((response) => response)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getPortfolioProduct(businessOwnerName: string, businessOwnerLob: string, productId: string): Observable<BuildingBlockMetricSummary> {
    return this.http.get<BuildingBlockMetricSummary>(`${environment.apiUrl}/portfolio/${businessOwnerName}/${businessOwnerLob}/products/${productId}`)
      .catch((error) => {
        console.log(error);
        return Observable.throw(error);
      });
  }

  getProductComponents(businessOwnerName: string, businessOwnerLob: string, productId: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary>(`${environment.apiUrl}/portfolio/${businessOwnerName}/${businessOwnerLob}/products/${productId}/components`)
      .catch(error => {
        console.log(error);
        return Observable.throw(error);
      });
  }
}
