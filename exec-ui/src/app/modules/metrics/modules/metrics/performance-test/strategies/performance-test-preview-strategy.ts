import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PerformanceTestTrendStrategy} from './performance-test-trend-strategy';
import {PerformanceTestPrimaryMetricStrategy} from './performance-test-primary-metric-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {PerformanceTestConfiguration} from '../performance-test.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricSummary} from "../../../shared/domain-models/metric-summary";

@Injectable()
export class PerformanceTestPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: PerformanceTestPrimaryMetricStrategy,
               private trendStrategy: PerformanceTestTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.isRatio = true;
    metricPreview.description = PerformanceTestConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricPreview.id = PerformanceTestConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model.summary);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model.summary);
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: PerformanceTestConfiguration.previewHeading,
        value: valueModel.value,
      };
    }
  }

    private calculateSecondaryMetric(model: MetricSummary) {
        const validSet = new Set(['Error Rate']);
        const sums = model.counts
            .filter(count => validSet.has(count.label['type']))
            .reduce((runningSums, count) => {
                return runningSums.set(count.label['type'], count.value);
            }, new Map());

        let result = [];

       /* if (sums.get('Transaction Per Second')) {
            result.push({name: 'Transaction Per Second', value: Math.round(sums.get('Transaction Per Second').toLocaleString())});
        }

        if (sums.get('Avg Response Times')) {
            result.push({name: 'Avg Response Times', value: sums.get('Avg Response Times').toLocaleString()});
        }*/

        if (sums.get('Error Rate')) {
            result.push({name: 'Error Rate', value: sums.get('Error Rate').toLocaleString(),unit:'%'});
        }
        return result;
    }
}
