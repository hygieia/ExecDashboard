import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {WorkInProgressTrendStrategy} from './work-in-progress-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {WorkInProgressPrimaryMetricStrategy} from './work-in-progress-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {WorkInProgressConfiguration} from '../work-in-progress.configuration';


@Injectable()
export class WorkInProgressGraphStrategy extends GraphStrategyBase {

  constructor (private primaryMetricStrategy: WorkInProgressPrimaryMetricStrategy,
               private trendStrategy: WorkInProgressTrendStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);

    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = WorkInProgressConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 ticket' : x.toLocaleString() + ' tickets';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['Epic', 'Story', 'Bugs' ,'Other']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
  }
  
  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
  	 const validSet = new Set(['Epic', 'Story', 'Bugs' ,'Other']);
    var timeMap = new Map<number, number>();
    for(let time of timeSeries){
      var count =  time.counts.reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo,count);
    }
    return timeMap;
  }
  
}
