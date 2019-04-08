import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {WorkInProgressPrimaryMetricStrategy} from './work-in-progress-primary-metric-strategy';
import {WorkInProgressTrendStrategy} from './work-in-progress-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {WorkInProgressConfiguration} from '../work-in-progress.configuration';

@Injectable()
export class WorkInProgressPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: WorkInProgressPrimaryMetricStrategy,
               private trendStrategy: WorkInProgressTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = WorkInProgressConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = WorkInProgressConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = 'NA';
    metricPreview.dataSource = WorkInProgressConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: WorkInProgressConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        unit: getUnit(valueModel.value),
        unitKey: 'In Progress Counts'
      };
    }
    
    function formattedData(value){
      if (value >= 1000) {
        return Math.round(value / 1000);
      }
      return Math.round(value);
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
    
    if (sums.get('Epic')) {
      var value = sums.get('Epic');
      if (value >= 1000) {
        value = Math.round(value / 1000) + ' k';
      }
      return [{name: 'Epic', value: value.toLocaleString()}];
    }

    if (sums.get('Story')) {
      var value = sums.get('Story');
      if (value >= 1000) {
        value = Math.round(value / 1000) + ' k';
      }
      return [{name: 'Story', value: value.toLocaleString()}];
    }

    if (sums.get('Bugs')) {
      var value = sums.get('Bugs');
      if (value >= 1000) {
        value = Math.round(value / 1000) + ' k';
      }
      return [{name: 'Bugs', value: value.toLocaleString()}];
    }


    if (sums.get('Other')) {
      var value = sums.get('Other');
      if (value >= 1000) {
        value = Math.round(value / 1000) + ' k';
      }
      return [{name: 'Other', value: value.toLocaleString()}];
    }

    return [];
  }
}
