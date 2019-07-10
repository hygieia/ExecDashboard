import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { MetricDetail } from '../domain-models/metric-detail';
import { MetricSummary } from '../domain-models/metric-summary';
import { BuildingBlockMetricSummary } from '../domain-models/building-block-metric-summary';
import { environment } from '../../../../../../environments/environment';



export abstract class MetricService {
  private readonly baseUri = `${environment.apiUrl}/metrics`;
  protected abstract metricType: string;
  protected readonly requestUrl = (resourceUri: string) =>
    `${this.baseUri}${resourceUri.charAt(0) === '/' ? resourceUri : '/' + resourceUri}`
  protected readonly portfolioResource = (id: string, resourceType: string) =>
    `${this.metricType}/portfolio/${id}/${resourceType}`
  protected readonly productResource = (id: string, resourceType: string) =>
    `${this.metricType}/product/${id}/${resourceType}`

  constructor(http: HttpClient) { }

  public abstract getPortfolioSummary(id: string): Observable<MetricSummary>;
  public abstract getPortfolioDetail(id: string): Observable<MetricDetail>;
  public abstract getPortfolioProducts(id: string): Observable<BuildingBlockMetricSummary[]>;

  public abstract getProductSummary(id: string): Observable<MetricSummary>;
  public abstract getProductDetail(id: string): Observable<MetricDetail>;
  public abstract getProductComponents(id: string): Observable<BuildingBlockMetricSummary[]>;

}
