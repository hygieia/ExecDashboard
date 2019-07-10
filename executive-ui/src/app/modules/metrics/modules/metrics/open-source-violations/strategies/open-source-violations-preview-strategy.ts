import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { OpenSourceViolationsTrendStrategy } from './open-source-violations-trend-strategy';
import { OpenSourceViolationsPrimaryMetricStrategy } from './open-source-violations-primary-metric-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { Injectable } from '@angular/core';
import { OpenSourceViolationsConfiguration } from '../open-source-violations.configuration';

@Injectable()
export class OpenSourceViolationsPreviewStrategy extends PreviewStrategyBase {

  constructor(private trendStrategy: OpenSourceViolationsTrendStrategy,
    private primaryMetricStrategy: OpenSourceViolationsPrimaryMetricStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = OpenSourceViolationsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = OpenSourceViolationsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = 'NA';
    metricPreview.dataSource = OpenSourceViolationsConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: OpenSourceViolationsConfiguration.previewHeading,
        value: Math.round(valueModel.value * 100) / 100,
        prefix: '~',
        unitKey: 'Days per story point'
      };
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

    const totalTime = sums.get('Total Time');
    const totalStoryPoints = sums.get('Total Story Points');

    const storyPointsDisplay = [{
      name: 'Total Story Points',
      value: totalStoryPoints
    }];

    return [{ name: 'Total Time', value: totalTime, unit: ' d' }, ...storyPointsDisplay];
  }
}
