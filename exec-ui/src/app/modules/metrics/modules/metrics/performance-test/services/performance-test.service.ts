import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {Injectable} from '@angular/core';
import {MetricService } from '../../../shared/services/metric.service';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import 'rxjs/observable/throw';
import {PerformanceTestConfiguration} from '../performance-test.configuration';

@Injectable()
export class PerformanceTestService extends MetricService {
  protected metricType = PerformanceTestConfiguration.identifier;

  constructor(private http: HttpClient) { super(http); }

  getPortfolioSummary(name: string, lob: string): Observable<MetricSummary> {
    return this.http.get<MetricSummary>(this.requestUrl(this.portfolioResource(name, lob, 'summary')))
      .map(response => response)
      .catch(error =>  { console.log(error); return Observable.throw(error); });
  }

  getPortfolioDetail(name: string, lob: string): Observable<MetricDetail> {
    return this.http.get<MetricSummary>(this.requestUrl(this.portfolioResource(name, lob, 'detail')))
      .map(response => response)
      .catch(error =>  { console.log(error); return Observable.throw(error); });
  }

  getPortfolioProducts(name: string, lob: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<MetricSummary>(this.requestUrl(this.portfolioResource(name, lob, 'product')))
      .map(response => response)
      .catch(error =>  { console.log(error); return Observable.throw(error); });
  }

  getProductSummary(name: string, lob: string, id: string): Observable<MetricSummary> {
    return this.http.get<MetricSummary>(this.requestUrl(this.productResource(name, lob, id, 'summary')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getProductDetail(name: string, lob: string, id: string): Observable<MetricDetail> {
    return this.http.get<MetricDetail>(this.requestUrl(this.productResource(name, lob, id, 'detail')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }

  getProductComponents(name: string, lob: string, id: string): Observable<BuildingBlockMetricSummary[]> {
    return this.http.get<BuildingBlockMetricSummary[]>(this.requestUrl(this.productResource(name, lob, id, 'component')))
      .map(response => response)
      .catch(error => { console.log(error); return Observable.throw(error); });
  }
}
