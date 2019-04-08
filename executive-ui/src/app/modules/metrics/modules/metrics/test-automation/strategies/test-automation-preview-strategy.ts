import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {TestAutomationTrendStrategy} from './test-automation-trend-strategy';
import {TestAutomationPrimaryMetricStrategy} from './test-automation-primary-metric-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {TestAutomationConfiguration} from '../test-automation.configuration';

@Injectable()
export class TestAutomationPreviewStrategy extends PreviewStrategyBase {

  constructor (private primaryMetricStrategy: TestAutomationPrimaryMetricStrategy,
               private trendStrategy: TestAutomationTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.isRatio = true;
    metricPreview.description = TestAutomationConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = TestAutomationConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = [];
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: TestAutomationConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }
}
