import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {SecurityViolationsTrendStrategy} from './security-violations-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SecurityViolationsPrimaryMetricStrategy} from './security-violations-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {SecurityViolationsConfiguration} from '../security-violations.configuration';

@Injectable()
export class SecurityViolationsGraphStrategy extends GraphStrategyBase {

  constructor (private primaryMetricStrategy: SecurityViolationsPrimaryMetricStrategy,
               private trendStrategy: SecurityViolationsTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);

    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = SecurityViolationsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 violation' : x.toLocaleString() + ' violations';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['blocker', 'critical', 'major']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
  }
}
