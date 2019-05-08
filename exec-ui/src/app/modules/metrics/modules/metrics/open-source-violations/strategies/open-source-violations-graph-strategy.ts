import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {OpenSourceViolationsTrendStrategy} from './open-source-violations-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {OpenSourceViolationsPrimaryMetricStrategy} from './open-source-violations-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {OpenSourceViolationsConfiguration} from '../open-source-violations.configuration';

@Injectable()
export class OpenSourceViolationsGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: OpenSourceViolationsTrendStrategy,
              private primaryMetricStrategy: OpenSourceViolationsPrimaryMetricStrategy) { super(); }
  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();
    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.valueLabel = OpenSourceViolationsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 violation' : x.toLocaleString() + ' violations';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['Critical', 'High', 'Medium', 'Low']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);
  }
}
