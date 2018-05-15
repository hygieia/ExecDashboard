import {EventEmitter, Input, Output} from '@angular/core';
import {MetricPreviewModel} from '../../component-models/metric-preview-model';
import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricService} from '../../services/metric.service';
import {MetricSummary} from '../../domain-models/metric-summary';
import {Observable} from 'rxjs/Observable';
import {Router} from '@angular/router';
import {ItemType} from '../../component-models/item-type';
/**
 *This is a base component class for displaying component instances in the metric previews container component.
 *Extend this class to build previews for additional metrics.
 * @export
 * @abstract
 * @class MetricPreviewBaseComponent
 */
export abstract class MetricPreviewBaseComponent {
  @Input() public portfolioName: string;
  @Input() public portfolioLob: string;
  @Input() public productId: string;
  @Input() public strategy: Strategy<any, MetricPreviewModel>;
  @Input() public selectedMetric: string;
  @Input() public sort: number;
  @Output() public hideBuildingBlocksEvent = new EventEmitter<boolean>();
  @Output() public showBuildingBlocksEvent = new EventEmitter<{}>();
  @Output() public isSelectedEvent = new EventEmitter<string>();
  @Output() public metricPreviewInitialized = new EventEmitter<boolean>();

  public dataService: MetricService;
  public metricPreview: MetricPreviewModel;
  public metric: string;
  public showDescription: boolean;
  public svgFillColor = '#FFFFFF';

  protected router: Router;

  constructor() { }

  click(event: Event): void {
    if (this.hasFocus()) {
      event.stopPropagation();
    }
  }

  goToDetails(event: Event): void {
    event.stopPropagation();
    return this.hasReports()
      ? this.viewDetails()
      : null;
  }

  viewDetails(): void {
    if (this.productId) {
      this.router.navigate(['portfolio', this.portfolioName, this.portfolioLob, this.metric, 'product', this.productId]);
    } else {
      this.router.navigate(['portfolio', this.portfolioName, this.portfolioLob, this.metric]);
    }
  }

  getSelectedAccentColor(): string {
    if (this.isSelected()) {
      return this.setFillColor();
    }
  }

  private setFillColor(): string {
    switch (this.metricPreview.trend.direction) {
      case 'neutral':
        return '#4A90E2';
      default:
        return this.metricPreview.trend.danger
          ? '#C21C36'
          : '#7ED321';
    }
  }

  setSvgFillColor(): void {
    this.svgFillColor = this.setFillColor();
  }

  resetSvgFillColor(): void {
    this.svgFillColor = '#FFFFFF';
  }

  hasFocus(): boolean {
    return this.selectedMetric
      ? this.isSelected()
      : true;
  }

  isSelected(): boolean {
    return this.metric === this.selectedMetric;
  }

  hasMetricData(): boolean {
    return !!this.metricPreview.score;
  }

  hasReports(): boolean {
    return !!this.metricPreview.totalReporting;
  }

  loadMetricSummaryData(): void {
    this.getMetricSummaryData()
      .subscribe(
        result => {
          this.metricPreview = this.strategy.parse(result);
          this.metricPreviewInitialized.emit(true);
        },
        error => {
          console.log(error);
        }
      );
  }

  toggleDetails(event: Event): void {
    return this.hasFocus()
      ? this.goToDetails(event)
      : undefined;
  }

  toggleBuildingBlocks(event: Event): void {
    return this.hasFocus()
      ? this.selectAndShowBuildingBlocksList(event)
      : undefined;
  }

  hideBuildingBlocks(): void {
    this.hideBuildingBlocksEvent.emit(true);
  }

  showBuildingBlocks(): void {
    this.showBuildingBlocksEvent.emit({
                                  'metric' : this.metricPreview.id,
                                  'listingType' : this.productId ? ItemType.component : ItemType.product
                                });
  }

  select(): void {
    this.isSelectedEvent.emit(this.metric);
  }

  selectAndShowBuildingBlocksList(event: Event): void {
    event.stopPropagation();
    if (this.isSelected()) {
      this.hideBuildingBlocks();
    } else {
      this.select();
      this.showBuildingBlocks();
    }
  }

  toggleDescription(): void {
    if (this.hasFocus()) {
      this.showDescription = !this.showDescription;
    }
  }

  showSmallScore(): boolean {
    const length = [
      this.metricPreview.score.prefix ? this.metricPreview.score.prefix.length : 0,
      this.metricPreview.score.value ? this.metricPreview.score.value.toString().length : 0,
      this.metricPreview.score.unit ? this.metricPreview.score.unit.length : 0,
      this.metricPreview.isRatio ? 1 : 0
    ].reduce((a, b) => a + b);

    return length >= 4;
  }

  private getMetricSummaryData(): Observable<MetricSummary> {
    if (this.productId) {
      return this.dataService.getProductSummary(this.portfolioName, this.portfolioLob, this.productId);
    }

    return this.dataService.getPortfolioSummary(this.portfolioName, this.portfolioLob);
  }
}
