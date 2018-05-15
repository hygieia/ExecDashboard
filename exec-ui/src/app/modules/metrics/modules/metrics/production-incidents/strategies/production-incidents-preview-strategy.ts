import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ProductionIncidentsPrimaryMetricStrategy} from './production-incidents-primary-metric-strategy';
import {ProductionIncidentsTrendStrategy} from './production-incidents-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {ProductionIncidentsAuxiliaryFigureStrategy} from './production-incidents-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';
import {ProductionIncidentsConfiguration} from '../production-incidents.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';

@Injectable()
export class ProductionIncidentsPreviewStrategy extends PreviewStrategyBase {

  constructor (private auxiliaryFigureStrategy: ProductionIncidentsAuxiliaryFigureStrategy,
              private primaryMetricStrategy: ProductionIncidentsPrimaryMetricStrategy,
              private trendStrategy: ProductionIncidentsTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = ProductionIncidentsConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricPreview.id = ProductionIncidentsConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model.summary);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model.summary);
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: ProductionIncidentsConfiguration.previewHeading,
        value: valueModel.value
      };
    }
  }

  private calculateSecondaryMetric(model: MetricSummary) {
    const severities = new Set(['1', '2', '3', '3C', '3D']);

    const open = model.counts
      .filter(count => count.label['type'] === 'issue')
      .filter(count => severities.has(count.label['severity']))
      .filter(count => count.label['event'] === 'open')
      .reduce((sum, count) => sum + count.value, 0);

    const closed = model.counts
      .filter(count => count.label['type'] === 'issue')
      .filter(count => severities.has(count.label['severity']))
      .filter(count => (count.label['event'] === 'close')
                                  || (count.label['event'] === 'closed')
                                  || (count.label['event'] === 'resolved'))
      .reduce((sum, count) => sum + count.value, 0);

    const mttr = this.auxiliaryFigureStrategy.parse(model);
    const mttrDisplay = !mttr.hasData ? [] : [{
      name: 'mean time to resolve',
      value: mttr.value,
      unit: unit(mttr)
    }];

    return [{ name: 'Open Incidents', value: open - closed }, ...mttrDisplay];

    function unit(valueModel) {
      switch (valueModel.unit) {
        case 'days': return 'd';
        case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
        case 'minutes': return 'm';
      }
    }
  }
}
