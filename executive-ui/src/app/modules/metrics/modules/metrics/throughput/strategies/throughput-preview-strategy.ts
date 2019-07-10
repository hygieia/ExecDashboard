import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ThroughputTrendStrategy} from './throughput-trend-strategy';
import {ThroughputPrimaryMetricStrategy} from './throughput-primary-metric-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {ThroughputConfiguration} from '../throughput.configuration';

@Injectable()
export class ThroughputPreviewStrategy extends PreviewStrategyBase {
  
  constructor (private trendStrategy: ThroughputTrendStrategy,
              private primaryMetricStrategy: ThroughputPrimaryMetricStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = ThroughputConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = ThroughputConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = 'NA';
    metricPreview.dataSource = ThroughputConfiguration.dataSource;
    return metricPreview;
    
     function mapPrimary(valueModel) {
      return {
        name: ThroughputConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        unit: getUnit(valueModel.value),
        unitKey: 'Stories Completed'
      };
    }
    
    function formattedData(value){
      if (value >= 1000) {
        return Math.round((value / 1000) * 100)/100;
      }
      return value;
  	}

    function getUnit(value){
      if (value >= 1000) {
        return 'k';
      }
      return '';
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['type'])) {
        runningSums.set(count.label['type'], 0);
      }
      const newCount = runningSums.get(count.label['type']) + count.value;
      return runningSums.set(count.label['type'], newCount);
    }, new Map());
    
    if (sums.get('Total Time')) {
      var value = sums.get('Total Time') ;
      if (value > 0) {
        value = value + ' d';
      }
      return [{name: 'Total Time', value: value.toLocaleString()}];
    }

     return [];

  }
}
