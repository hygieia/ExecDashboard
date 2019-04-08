import {EventEmitter, Input, Output} from '@angular/core';
import {MetricPreviewModel} from '../../component-models/metric-preview-model';
import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricService} from '../../services/metric.service';
import {MetricSummary} from '../../domain-models/metric-summary';
import {Observable} from 'rxjs/Observable';
import {Router} from '@angular/router';
/**
 *This is a base component class for displaying component instances in the metric previews container component.
 *Extend this class to build previews for additional metrics.
 * @export
 * @abstract
 * @class MetricPreviewBaseComponent
 */
export abstract class MetricPreviewBaseComponent {
  @Input() public portfolioId: string;
  @Input() public productId: string;
  @Input() public strategy: Strategy<any, MetricPreviewModel>;
  @Input() public selectedMetric: string;
  @Input() public sort: number;
  @Output() public hideBuildingBlocksEvent = new EventEmitter<boolean>();
  @Output() public showBuildingBlocksEvent = new EventEmitter<string>();
  @Output() public isSelectedEvent = new EventEmitter<string>();
  @Output() public metricPreviewInitialized = new EventEmitter<boolean>();
  @Output() showSpinner =  new EventEmitter<any>();

  public dataService: MetricService;
  public metricPreview: MetricPreviewModel;
  public metric: string;
  public showDescription: boolean;
  public svgFillColor = '#FFFFFF';
  public isCycleTime = false;
  public isDisabled = false;
  public showCloseBtn: boolean = false;
  public dataAvailability:boolean = false;
  public isEventsOrSecurity: boolean = false;
  public isSecurity: boolean = false;
  public isBuildOrDeploy: boolean = false;
  public isCloud: boolean = false;
  public isCloudEnabled: boolean = false;

  protected router: Router;

  constructor() { 
    this.showSpinner.emit(true);
  }

  click(event: Event): void {
    if (this.hasFocus()) {
      event.stopPropagation();
    }
  }

  goToDetails(event: Event): void {
    event.stopPropagation();
    return this.hasReportsOne()
      ? this.viewDetails()
      : null;
  }

  viewDetails(): void {
    if (this.productId) {
      this.router.navigate(['portfolio', this.portfolioId, this.metric, 'product', this.productId]);
    } else {
      this.router.navigate(['portfolio', this.portfolioId, this.metric]);
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
    if(this.metric === this.selectedMetric){
      this.showCloseBtn = true;
      return true;
    }else{
       this.showCloseBtn = false;
       return false;
    }
     
  }

  hasMetricData(): boolean {
    return !!this.metricPreview.score;
  }

  hasReports(): boolean {
    return !!this.metricPreview.totalReporting;
  }
  
  hasReportsOne(): boolean {
    if (this.metricPreview.id === 'production-incidents' && this.metricPreview.score.value < 1) {
      return false;
    }
    return this.hasReports();
  }


  loadMetricSummaryData(): void {
    this.showSpinner.emit(true);
    this.getMetricSummaryData()
      .subscribe(
        result => {
          this.metricPreview = this.strategy.parse(result);
          if(this.metricPreview && this.productId && this.metricPreview.id == 'cloud'){
            this.metricPreview.secondaryMetrics[1].name = 'Prod Migration Enabled';
            this.isCloudEnabled = true;
              if(this.metricPreview.secondaryMetrics[1].value > 0 )
                this.metricPreview.secondaryMetrics[1].unit = 'Yes';
              else
                this.metricPreview.secondaryMetrics[1].unit = 'No';
          }
          this.modulesTab()
          this.metricPreviewInitialized.emit(true);
          this.showSpinner.emit(false);
        },
        error => {
          console.log(error);
          this.showSpinner.emit(false);
        }
      );
  }

   modulesTab(){
     
     if(this.metricPreview.available){
            this.dataAvailability =true;
            
      }else{
            this.dataAvailability = false;
           
      }
          if(this.metricPreview.id == 'production-incidents' || this.metricPreview.id == 'security-violations' || this.metricPreview.id == 'cloud' ){
            this.isEventsOrSecurity = true;
          }

          if(this.metricPreview.id == 'security-violations'){
            this.isSecurity = true;
          }

          if(this.metricPreview.id == 'build' || this.metricPreview.id == 'deploy'){
            this.isBuildOrDeploy = true;
          }

          if(this.productId && this.metricPreview.id == 'cloud'){
            this.isCloud = true;
          }
  }
  toggleDetails(event: Event): void {
    return this.hasFocus()
      ? this.goToDetails(event)
      : undefined;
  }

  toggleBuildingBlocks(event: Event): void {
    return ((this.hasFocus() && this.dataAvailability && !this.isEventsOrSecurity) || (this.hasFocus() && !this.productId))
      ? this.selectAndShowBuildingBlocksList(event)
      : undefined;
  }

  hideBuildingBlocks(): void {
    this.hideBuildingBlocksEvent.emit(true);
  }

  showBuildingBlocks(): void {
    this.showBuildingBlocksEvent.emit(this.metricPreview.id);
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
    
    if(this.metricPreview.id == 'pipeline-lead-time'){
      this.isCycleTime = true;
    }else{
      this.isCycleTime = false;
    }
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
      return this.dataService.getProductSummary(this.productId);
    }
    return this.dataService.getPortfolioSummary(this.portfolioId);
  }
}

