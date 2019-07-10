import { Injectable } from '@angular/core';
import { MetricService } from '../../../shared/services/metric.service';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { DevopsCupConfiguration } from '../devopscup.configuration';
import { DevopscupScores } from '../../../../../shared/domain-models/devopscupScores';
import { Vast } from '../../../../../shared/domain-models/vast';
import { environment } from '../../../../../../../environments/environment';
import { DevopscupRoundDetails } from '../../../../../shared/domain-models/devopscupRoundDetails';

@Injectable()
export class DevopscupService extends MetricService {

  protected metricType = DevopsCupConfiguration.api;
  protected baseUrl = `${environment.apiUrl}`;
  protected readonly vastDevopscup = this.baseUrl + '/vast/devopscup';
  protected readonly baseDevopscup = this.baseUrl + '/metrics/roundForDevopscup';
  constructor(private http: HttpClient) { super(http); }

  getPortfolioSummary(id: string): Observable<MetricSummary> {
    return this.http.get<MetricSummary>(this.requestUrl(this.portfolioResource(id, 'summary')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getPortfolioDetail(id: string): Observable<MetricDetail> {
    return this.http.get<MetricSummary>(this.requestUrl(this.portfolioResource(id, 'detail')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getPortfolioProducts(id: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<MetricSummary>(this.requestUrl(this.portfolioResource(id, 'product')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getProductSummary(id: string): Observable<MetricSummary> {
    return this.http.get<MetricSummary>(this.requestUrl(this.productResource(id, 'summary')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getProductDetail(id: string): Observable<MetricDetail> {
    return this.http.get<MetricDetail>(this.requestUrl(this.productResource(id, 'detail')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getProductComponents(id: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(this.requestUrl(this.productResource(id, 'component')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }
  getPortfolioProductsForDevopscup(id: string): Observable<DevopscupScores[]> {
    return this.http.get<DevopscupScores[]>(this.requestUrl(this.portfolioResource(id, 'productForDevopscup')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getVastDetailsForDevopscup(): Observable<Map<string, Vast>> {
    return this.http.get<Map<string, Vast>>(this.vastDevopscup)
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });

  }
  getRoundDetailsForDevopscup(): Observable<DevopscupRoundDetails> {
    return this.http.get<DevopscupRoundDetails>(this.baseDevopscup)
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });

  }

}
