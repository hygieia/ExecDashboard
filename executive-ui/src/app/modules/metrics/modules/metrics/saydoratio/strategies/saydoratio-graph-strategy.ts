import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {SayDoRatioTrendStrategy} from './saydoratio-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SayDoRatioPrimaryMetricStrategy} from './saydoratio-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {SayDoRatioConfiguration} from '../saydoratio.configuration';

@Injectable()
export class SayDoRatioGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: SayDoRatioTrendStrategy,
              private primaryMetricStrategy: SayDoRatioPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

   
    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = SayDoRatioConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 StoryPoints Ratio' : x.toLocaleString() + ' StoryPoints Ratio';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    
    return seriesElement.counts
      .reduce((sum, item) => sum + item.value , 0);
  }

  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
    var timeMap = new Map<number, number>();
    for(let time of timeSeries){
      var count =  time.counts.reduce((sum, item) => sum + item.value, 0 );
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo,count);
    }
    return timeMap;
  }
}
