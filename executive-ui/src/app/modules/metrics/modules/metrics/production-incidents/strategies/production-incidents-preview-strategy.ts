import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { ProductionIncidentsPrimaryMetricStrategy } from './production-incidents-primary-metric-strategy';
import { ProductionIncidentsTrendStrategy } from './production-incidents-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { ProductionIncidentsAuxiliaryFigureStrategy } from './production-incidents-auxiliary-figure-strategy';
import { Injectable } from '@angular/core';
import { ProductionIncidentsConfiguration } from '../production-incidents.configuration';

@Injectable()
export class ProductionIncidentsPreviewStrategy extends PreviewStrategyBase {

  constructor(private auxiliaryFigureStrategy: ProductionIncidentsAuxiliaryFigureStrategy,
    private primaryMetricStrategy: ProductionIncidentsPrimaryMetricStrategy,
    private trendStrategy: ProductionIncidentsTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = ProductionIncidentsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = ProductionIncidentsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.dataSource = ProductionIncidentsConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: ProductionIncidentsConfiguration.previewHeading,
        value: valueModel.value,
        unitKey: 'Production Events'
      };
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const severities = new Set(['1']);

    const open = 0;

    const closed = model.counts
      //.filter(count => count.label['type'] === 'issue')
      .filter(count => severities.has(count.label['severity']))
      //.filter(count => count.label['event'] === 'closed')
      .reduce((sum, count) => sum + count.value, 0);

    const mttr = this.auxiliaryFigureStrategy.parse(model);
    const mttrDisplay = !mttr.hasData ? [] : [{
      name: 'mean time to resolve',
      value: mttr.value,
      unit: unit(mttr)
    }];

    return [{ name: 'Total Outages', value: closed }, ...mttrDisplay];

    function unit(valueModel) {
      switch (valueModel.unit) {
        case 'days': return ' d';
        case 'hours': return valueModel.value === 1 ? ' hr' : ' hrs';
        case 'minutes': return ' m';
      }
    }
  }
}
