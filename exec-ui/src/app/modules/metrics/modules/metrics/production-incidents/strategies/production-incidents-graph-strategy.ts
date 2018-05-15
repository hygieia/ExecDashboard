import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {ProductionIncidentsTrendStrategy} from './production-incidents-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ProductionIncidentsPrimaryMetricStrategy} from './production-incidents-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {ProductionIncidentsConfiguration} from '../production-incidents.configuration';

@Injectable()
export class ProductionIncidentsGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: ProductionIncidentsTrendStrategy,
              private primaryMetricStrategy: ProductionIncidentsPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = ProductionIncidentsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 issue' : x.toLocaleString() + ' issues';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['1', '2', '3', '3C', '3D']);
    return seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => item.label['event'] === 'open')
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);
  }
}
