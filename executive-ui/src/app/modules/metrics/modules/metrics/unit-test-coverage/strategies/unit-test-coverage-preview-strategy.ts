import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {UnitTestCoverageTrendStrategy} from './unit-test-coverage-trend-strategy';
import {UnitTestCoveragePrimaryMetricStrategy} from './unit-test-coverage-primary-metric-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {UnitTestCoverageConfiguration} from '../unit-test-coverage.configuration';

@Injectable()
export class UnitTestCoveragePreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: UnitTestCoveragePrimaryMetricStrategy,
               private trendStrategy: UnitTestCoverageTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.isRatio = true;
    metricPreview.description = UnitTestCoverageConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = UnitTestCoverageConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = [];
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: UnitTestCoverageConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }
}
