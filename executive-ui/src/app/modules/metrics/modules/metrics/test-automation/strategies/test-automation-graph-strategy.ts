import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {TestAutomationTrendStrategy} from './test-automation-trend-strategy';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import decimalToPercent = PresentationFunctions.decimalToPercent;
import {TestAutomationPrimaryMetricStrategy} from './test-automation-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {TestAutomationConfiguration} from '../test-automation.configuration';

@Injectable()
export class TestAutomationGraphStrategy extends GraphStrategyBase {

  constructor (private primaryMetricStrategy: TestAutomationPrimaryMetricStrategy,
               private trendStrategy: TestAutomationTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = new Map<number, number>();
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = true;
    metricGraph.valueLabel = TestAutomationConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = (x) => `${x}% passing`;

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    return decimalToPercent(seriesElement.counts[0].value);
  }
}
