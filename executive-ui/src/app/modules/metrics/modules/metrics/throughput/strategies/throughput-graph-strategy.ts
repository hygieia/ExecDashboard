import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {ThroughputTrendStrategy} from './throughput-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ThroughputPrimaryMetricStrategy} from './throughput-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {ThroughputConfiguration} from '../throughput.configuration';

@Injectable()
export class ThroughputGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: ThroughputTrendStrategy,
              private primaryMetricStrategy: ThroughputPrimaryMetricStrategy) { super(); }
  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.valueLabel = ThroughputConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 Story' : x.toLocaleString() + ' Stories';
    return metricGraph;
  }


  
   protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    const validSet = new Set(['Total Story Points']);
    const validSet1 = new Set(['Total Time']);
    const validSet2 = new Set(['Total User Stories']);
    const validSet3 = new Set(['Total Stories']); 

    var timeMap = new Map<number, number>();

    for(let time of timeSeries){
      var countTotalStories =  time.counts.reduce((sum, item) => validSet3.has(item.label['type']) ? sum + item.value : sum, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo,countTotalStories);
    }

    for(let time of timeSeries){
      
      var countsA = time.counts
      var countsB = countsA.filter(d => { return d.label['type'] == 'Total User Stories'})

      var count = Number(countsB[0].value)
      var daysAgo = time.daysAgo + 90
      //timeMap.set(daysAgo, count);
    }



    return timeMap;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['Total Story Points']);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['type']) ? sum + item.value : sum, 0);
  }
}
