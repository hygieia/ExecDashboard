import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {OpenSourceViolationsTrendStrategy} from './open-source-violations-trend-strategy';
import {OpenSourceViolationsPrimaryMetricStrategy} from './open-source-violations-primary-metric-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {OpenSourceViolationsConfiguration} from '../open-source-violations.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';

@Injectable()
export class OpenSourceViolationsPreviewStrategy extends PreviewStrategyBase {
  constructor (private trendStrategy: OpenSourceViolationsTrendStrategy,
              private primaryMetricStrategy: OpenSourceViolationsPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = OpenSourceViolationsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricPreview.id = OpenSourceViolationsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model.summary);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model.summary);
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: OpenSourceViolationsConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['severity'])) {
        runningSums.set(count.label['severity'], 0);
      }
      const newCount = runningSums.get(count.label['severity']) + count.value;
      return runningSums.set(count.label['severity'], newCount);
    }, new Map());

    if (sums.get('Critical')) {
          return [{name: 'Critical', value: sums.get('Critical').toLocaleString()}];
      }

      if (sums.get('High')) {
      return [{name: 'High', value: sums.get('High').toLocaleString()}];
    }

    if (sums.get('Medium')) {
      return [{name: 'Medium', value: sums.get('Medium').toLocaleString()}];
    }

    if (sums.get('Low')) {
      return [{name: 'Low', value: sums.get('Low').toLocaleString()}];
    }

    return [];
  }
}
