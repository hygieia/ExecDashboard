import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {SCMCommitsTrendStrategy} from './scm-commits-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SCMCommitsPrimaryMetricStrategy} from './scm-commits-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {SCMCommitsConfiguration} from '../scm-commits.configuration';

@Injectable()
export class SCMCommitsGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: SCMCommitsTrendStrategy,
              private primaryMetricStrategy: SCMCommitsPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.fillInMissingTimeSeriesElements(model.timeSeries);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = SCMCommitsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 code commit' : x.toLocaleString() + ' code commits';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    return seriesElement.counts
      .reduce((sum, item) => sum + item.value, 0);
  }
}
