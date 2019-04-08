import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { PipelineLeadTimeTrendStrategy } from './pipeline-lead-time-trend-strategy';
import { PipelineLeadTimePrimaryMetricStrategy } from './pipeline-lead-time-primary-metric-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { Injectable } from '@angular/core';
import { PipelineLeadTimeConfiguration } from '../pipeline-lead-time.configuration';

@Injectable()
export class PipelineLeadTimePreviewStrategy extends PreviewStrategyBase {

  constructor(private primaryMetricStrategy: PipelineLeadTimePrimaryMetricStrategy,
    private trendStrategy: PipelineLeadTimeTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {

    const metricPreview = new MetricPreviewModel();
    metricPreview.isRatio = false;
    metricPreview.description = PipelineLeadTimeConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = PipelineLeadTimeConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = 'NA';
    metricPreview.dataSource = PipelineLeadTimeConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: PipelineLeadTimeConfiguration.previewHeading,
        value: valueModel.value,
        prefix: '~',
        unitKey: units(valueModel)
      };
    }

    function units(valueModel) {
      switch (valueModel.unit) {
        case 'days': return 'Days';
        case 'hours': return valueModel.value === 1 ? 'Hour' : 'Hours';
        case 'minutes': return 'Minutes';
      }
    }

    function unit(valueModel) {
      switch (valueModel.unit) {
        case 'days': return 'd';
        case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
        case 'minutes': return 'm';
      }
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

    if (sums.get('cadence') != null) {
      var value = sums.get('cadence');

      if (value == "0NaN") {
        value = 0;
      }

      if (value > 0) {
        value = Math.round(value * 100 / 100) + ' d';
      }
      return [{ name: 'Deployment Cadence', value: value.toLocaleString() }];
    }
    return [];
  }

}
