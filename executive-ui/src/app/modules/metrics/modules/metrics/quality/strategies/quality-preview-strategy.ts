import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { QualityPrimaryMetricStrategy } from './quality-primary-metric-strategy';
import { QualityTrendStrategy } from './quality-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { Injectable } from '@angular/core';
import { QualityConfiguration } from '../quality.configuration';

@Injectable()
export class QualityPreviewStrategy extends PreviewStrategyBase {

  constructor(private primaryMetricStrategy: QualityPrimaryMetricStrategy,
    private trendStrategy: QualityTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = QualityConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = QualityConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.message = 'NA';
    metricPreview.dataSource = QualityConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: QualityConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        unit: getUnit(valueModel.value),
        unitKey: 'Production Defects'
      };
    }

    function formattedData(value) {
      if (value >= 1000) {
        return Math.round(value / 1000);
      }
      return Math.round(value);
    }

    function getUnit(value) {
      if (value >= 1000) {
        return 'k';
      }
      return '';
    }
  }


  private calculateSecondaryMetric(model: MetricSummary) {
    const sums = model.counts.reduce((runningSums, count) => {
      if (!runningSums.has(count.label['priority'])) {
        runningSums.set(count.label['priority'], 0);
      }
      const newCount = runningSums.get(count.label['priority']) + count.value;
      return runningSums.set(count.label['priority'], newCount);
    }, new Map());

    const normal = sums.get('normal');
    const blocker = sums.get('blocker');
    const high = sums.get('high');
    const low = sums.get('low');
    const totalDefects = sums.get('total');
    var total = normal + blocker + high + low;
    if (total >= 1000) {
      total = Math.round(total / 1000) + ' k';
    }

    var sn = sums.get('serviceNow');
    if (sn >= 1000) {
      sn = Math.round(sn / 1000) + ' k';
    }

    const snDisplay = [{
      name: 'SN Defects',
      value: sn

    }];

    return [{ name: 'Total Jira Defects', value: total }, ...snDisplay];

  }
}
