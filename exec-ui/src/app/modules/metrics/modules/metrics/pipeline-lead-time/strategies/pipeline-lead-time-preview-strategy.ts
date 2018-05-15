import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PipelineLeadTimeTrendStrategy} from './pipeline-lead-time-trend-strategy';
import {PipelineLeadTimePrimaryMetricStrategy} from './pipeline-lead-time-primary-metric-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {PipelineLeadTimeConfiguration} from '../pipeline-lead-time.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';

@Injectable()
export class PipelineLeadTimePreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: PipelineLeadTimePrimaryMetricStrategy,
               private trendStrategy: PipelineLeadTimeTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.isRatio = false;
    metricPreview.description = PipelineLeadTimeConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricPreview.id = PipelineLeadTimeConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model.summary);
    metricPreview.secondaryMetrics = [];
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: PipelineLeadTimeConfiguration.previewHeading,
        value: valueModel.value,
        unit: unit(valueModel),
        prefix: valueModel.prefix
      };
    }

    function unit(valueModel) {
      switch (valueModel.unit) {
        case 'days': return 'd';
        case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
        case 'minutes': return 'm';
      }
    }
  }
}
