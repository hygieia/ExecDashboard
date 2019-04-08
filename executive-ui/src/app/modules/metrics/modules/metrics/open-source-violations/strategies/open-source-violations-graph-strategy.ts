import { MetricGraphModel } from '../../../shared/component-models/metric-graph-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { MetricTimeSeriesElement } from '../../../shared/domain-models/metric-time-series-element';
import { OpenSourceViolationsTrendStrategy } from './open-source-violations-trend-strategy';
import { GraphStrategyBase } from '../../../shared/strategies/graph-strategy-base';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { OpenSourceViolationsPrimaryMetricStrategy } from './open-source-violations-primary-metric-strategy';
import { Injectable } from '@angular/core';
import { OpenSourceViolationsConfiguration } from '../open-source-violations.configuration';

@Injectable()
export class OpenSourceViolationsGraphStrategy extends GraphStrategyBase {

  constructor(private trendStrategy: OpenSourceViolationsTrendStrategy,
    private primaryMetricStrategy: OpenSourceViolationsPrimaryMetricStrategy) { super(); }
  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.valueLabel = OpenSourceViolationsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 day per Story Point' : x.toLocaleString() + ' days per Story Point';
    return metricGraph;
  }



  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    const validSet = new Set(['Total Story Points']);
    const validSet1 = new Set(['Total Time']);
    const validSet2 = new Set(['Total Stories']);
    var timeMap = new Map<number, number>();

    for (let time of timeSeries) {
      var countStoryPoints = time.counts.reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
      var countTotalTime = time.counts.reduce((sum, item) => validSet1.has(item.label['type']) ? sum + item.value : sum, 0);
      var countTotalStories = time.counts.reduce((sum, item) => validSet2.has(item.label['type']) ? sum + item.value : sum, 0);
      var daysAgo = time.daysAgo;

      if (countStoryPoints != 0) {
        var velocity = Math.round(countTotalTime / countStoryPoints);
        timeMap.set(daysAgo, velocity);
      }
      else if (countStoryPoints == 0 && countTotalStories != 0) {
        var velocity = Math.round(countTotalTime / countTotalStories);
        timeMap.set(daysAgo, velocity);
      }
      else {
        var velocity = 0;
        timeMap.set(daysAgo, velocity);
      }
    }
    return timeMap;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['Total Story Points']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
  }
}
