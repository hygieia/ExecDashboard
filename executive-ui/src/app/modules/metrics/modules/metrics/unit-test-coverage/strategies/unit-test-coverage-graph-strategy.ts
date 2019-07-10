import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {UnitTestCoverageTrendStrategy} from './unit-test-coverage-trend-strategy';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import decimalToPercent = PresentationFunctions.decimalToPercent;
import {UnitTestCoveragePrimaryMetricStrategy} from './unit-test-coverage-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {UnitTestCoverageConfiguration} from '../unit-test-coverage.configuration';

@Injectable()
export class UnitTestCoverageGraphStrategy extends GraphStrategyBase {

  constructor (private primaryMetricStrategy: UnitTestCoveragePrimaryMetricStrategy,
               private trendStrategy: UnitTestCoverageTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = new Map<number, number>();
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = true;
    metricGraph.valueLabel = UnitTestCoverageConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = (x) => `${x}% coverage`;

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    return decimalToPercent(seriesElement.counts[0].value);
  }
}
