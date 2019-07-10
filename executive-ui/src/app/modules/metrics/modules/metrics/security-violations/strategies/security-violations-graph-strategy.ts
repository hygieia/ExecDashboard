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
    metricGraph.values = this.getMap(model.timeSeries);
    //model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = SecurityViolationsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 violation' : x.toLocaleString() + ' violations';

    return metricGraph;
  }

  protected count(seriesElement: MetricTimeSeriesElement): number {
    const validSet = new Set(['codeBlocker', 'codeCritical', 'codeMajor','portBlocker', 'portCritical', 'portMajor','webBlocker', 'webCritical', 'webMajor','blackDuckMajor','blackDuckCritical','blackDuckBlocker' ]);
    return seriesElement.counts
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);
  }
  
  protected getMap(timeSeries: MetricTimeSeriesElement[]): any {
  	 const validSet = new Set(['codeBlocker', 'codeCritical', 'codeMajor','portBlocker', 'portCritical', 'portMajor','webBlocker', 'webCritical', 'webMajor','blackDuckMajor','blackDuckCritical','blackDuckBlocker']);
    var timeMap = new Map<number, number>();
    for(let time of timeSeries){
      var count =  time.counts.reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);
      var daysAgo = time.daysAgo;
      timeMap.set(daysAgo,count);
    }
    return timeMap;
  }
  
}
