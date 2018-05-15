import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { MetricDetail } from '../domain-models/metric-detail';
import { MetricSummary } from '../domain-models/metric-summary';
import { BuildingBlockMetricSummary } from '../domain-models/building-block-metric-summary';
import {environment} from '../../../../../../environments/environment';

export abstract class MetricService {
  private readonly baseUri = `${environment.apiUrl}/metrics`;
  protected abstract metricType: string;
  protected readonly requestUrl = (resourceUri: string) =>
    `${this.baseUri}${resourceUri.charAt(0) === '/' ? resourceUri : '/' + resourceUri}`
  protected readonly portfolioResource = (name: string, lob: string, resourceType: string) =>
    `${this.metricType}/portfolio/${name}/${lob}/${resourceType}`
  protected readonly productResource = (name: string, lob: string, id: string, resourceType: string) =>
    `${this.metricType}/product/${name}/${lob}/${id}/${resourceType}`

  constructor(http: HttpClient) { }

  public abstract getPortfolioSummary(name: string, lob: string): Observable<MetricSummary>;
  public abstract getPortfolioDetail(name: string, lob: string): Observable<MetricDetail>;
  public abstract getPortfolioProducts(name: string, lob: string): Observable<BuildingBlockMetricSummary[]>;

  public abstract getProductSummary(name: string, lob: string, id: string): Observable<MetricSummary>;
  public abstract getProductDetail(name: string, lob: string, id: string): Observable<MetricDetail>;
  public abstract getProductComponents(name: string, lob: string, id: string): Observable<BuildingBlockMetricSummary[]>;
}
