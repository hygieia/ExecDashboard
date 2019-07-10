import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { Injectable } from '@angular/core';
import { MetricService } from '../../../shared/services/metric.service';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import { OpenSourceViolationsConfiguration } from '../open-source-violations.configuration';


@Injectable()
export class OpenSourceViolationsService extends MetricService {

  protected metricType = OpenSourceViolationsConfiguration.api;

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
}
