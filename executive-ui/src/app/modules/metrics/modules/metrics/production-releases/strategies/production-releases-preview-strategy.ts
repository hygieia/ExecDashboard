import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { ProductionReleasesPrimaryMetricStrategy } from './production-releases-primary-metric-strategy';
import { ProductionReleasesTrendStrategy } from './production-releases-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { Injectable } from '@angular/core';
import { ProductionReleasesConfiguration } from '../production-releases.configuration';

@Injectable()
export class ProductionReleasesPreviewStrategy extends PreviewStrategyBase {

  constructor(private primaryMetricStrategy: ProductionReleasesPrimaryMetricStrategy,
    private trendStrategy: ProductionReleasesTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = ProductionReleasesConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = ProductionReleasesConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = [];
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: ProductionReleasesConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }
}
