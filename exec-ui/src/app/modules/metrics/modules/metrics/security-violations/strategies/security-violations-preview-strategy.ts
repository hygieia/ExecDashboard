import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SecurityViolationsPrimaryMetricStrategy} from './security-violations-primary-metric-strategy';
import {SecurityViolationsTrendStrategy} from './security-violations-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {SecurityViolationsConfiguration} from '../security-violations.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';

@Injectable()
export class SecurityViolationsPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: SecurityViolationsPrimaryMetricStrategy,
               private trendStrategy: SecurityViolationsTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = SecurityViolationsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricPreview.id = SecurityViolationsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model.summary);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model.summary);
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: SecurityViolationsConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
      const validSet = new Set(['blocker', 'critical', 'major']);
      const sums = model.counts
          .filter(count => validSet.has(count.label['type']))
          .reduce((runningSums, count) => {
              return runningSums.set(count.label['type'], count.value);
          }, new Map());
    if (sums.get('blocker')) {
      return [{name: 'blocker', value: sums.get('blocker').toLocaleString()}];
    }

    if (sums.get('critical')) {
      return [{name: 'critical', value: sums.get('critical').toLocaleString()}];
    }

    if (sums.get('major')) {
      return [{name: 'major', value: sums.get('major').toLocaleString()}];
    }

    return [];
  }
}
