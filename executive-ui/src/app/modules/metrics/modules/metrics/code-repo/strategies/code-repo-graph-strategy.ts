import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { CodeRepoTrendStrategy } from './code-repo-trend-strategy';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { CodeRepoPrimaryMetricStrategy } from './code-repo-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { CodeRepoConfiguration } from '../code-repo.configuration';


@Injectable()
export class CodeRepoGraphStrategy extends GraphStrategyBase {

  constructor(private primaryMetricStrategy: CodeRepoPrimaryMetricStrategy,
    private trendStrategy: CodeRepoTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);

    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = CodeRepoConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 Code Commit' : x.toLocaleString() + ' Code Commits';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['totalCommits']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
  }

  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    const validSet = new Set(['totalCommits']);
    var timeMap = new Map<number, number>();
    for (let time of timeSeries) {
      var count = time.counts.reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo, count);
    }
    return timeMap;
  }

}
