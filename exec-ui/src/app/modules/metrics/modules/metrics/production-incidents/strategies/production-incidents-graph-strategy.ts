import {MetricGraphModel} from '../../../shared/component-models/metric-graph-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../../../shared/domain-models/metric-time-series-element';
import {ProductionIncidentsTrendStrategy} from './production-incidents-trend-strategy';
import {GraphStrategyBase} from '../../../shared/strategies/graph-strategy-base';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ProductionIncidentsPrimaryMetricStrategy} from './production-incidents-primary-metric-strategy';
import {Injectable} from '@angular/core';
import {ProductionIncidentsConfiguration} from '../production-incidents.configuration';
import {MetricSummary} from "../../../shared/domain-models/metric-summary";

@Injectable()
export class ProductionIncidentsGraphStrategy extends GraphStrategyBase {

  constructor (private trendStrategy: ProductionIncidentsTrendStrategy,
              private primaryMetricStrategy: ProductionIncidentsPrimaryMetricStrategy) { super(); }

  public parse(model: MetricDetail): MetricGraphModel {
    const metricGraph = new MetricGraphModel();

    metricGraph.trend = this.trendStrategy.parse(model.summary);
    metricGraph.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricGraph.score = this.primaryMetricStrategy.parse(model.summary.counts);
    metricGraph.additionalValues = model.timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.countBySeverity);
    metricGraph.isRatio = false;
    metricGraph.valueLabel = "OPEN "+ ProductionIncidentsConfiguration.graphHeading.toUpperCase();
    metricGraph.toolTipLabel = x => x === 1 ? '1 issue' : x.toLocaleString() + ' issues';

    return metricGraph;
  }

  protected countBySeverity(seriesElement: MetricTimeSeriesElement):
  { totalCount: number; item: string} {
    const validSet = new Set(['1', '2', '3', '3C', '3D']);

    let totalOpenIssues : number;
    totalOpenIssues = seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => ((!!item.label['event'].length)
                                  && ((item.label['event'].toLowerCase() === 'open')
                                      || (item.label['event'].toLowerCase() === 'opened'))))
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);

    let totalSeverity1OpenIssues : number;
    totalSeverity1OpenIssues = seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => ((!!item.label['event'].length)
            && ((item.label['event'].toLowerCase() === 'open')
              || (item.label['event'].toLowerCase() === 'opened')))
            && (item.label['severity'] === '1'))
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);

    let totalSeverity2OpenIssues: number;
    totalSeverity2OpenIssues = seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => ((!!item.label['event'].length)
        && ((item.label['event'].toLowerCase() === 'open')
          || (item.label['event'].toLowerCase() === 'opened')))
        && (item.label['severity'] === '2'))
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);

    let totalSeverity3OpenIssues : number;
    totalSeverity3OpenIssues = seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => ((!!item.label['event'].length)
        && ((item.label['event'].toLowerCase() === 'open')
          || (item.label['event'].toLowerCase() === 'opened')))
        && (item.label['severity'] === '3'))
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);

    let totalSeverity3COpenIssues : number;
    totalSeverity3COpenIssues = seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => ((!!item.label['event'].length)
        && ((item.label['event'].toLowerCase() === 'open')
          || (item.label['event'].toLowerCase() === 'opened')))
        && (item.label['severity'] === '3C'))
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);

    let totalSeverity3DOpenIssues : number;
    totalSeverity3DOpenIssues = seriesElement.counts
      .filter(item => item.label['type'] === 'issue')
      .filter(item => ((!!item.label['event'].length)
        && ((item.label['event'].toLowerCase() === 'open')
          || (item.label['event'].toLowerCase() === 'opened')))
        && (item.label['severity'] === '3D'))
      .reduce((sum, item) => validSet.has(item.label['severity']) ? sum + item.value : sum, 0);

    let itemVal : string;
    let separatorFlag : boolean = false;
    itemVal = totalOpenIssues + ' Total Issues: ';
    if (totalSeverity1OpenIssues > 0) {
      itemVal += (totalSeverity1OpenIssues + ' SEV 1 Issues ');
      separatorFlag = true;
    }
    if (totalSeverity2OpenIssues > 0) {
      itemVal += (separatorFlag)?(", " + totalSeverity2OpenIssues + ' SEV 2 Issues '):totalSeverity2OpenIssues + ' SEV 2 Issues ';
      separatorFlag = true;
    }
    if (totalSeverity3OpenIssues > 0) {
      itemVal += (separatorFlag)?(", " + totalSeverity3OpenIssues + ' SEV 3 Issues '):totalSeverity3OpenIssues + ' SEV 3 Issues ';
      separatorFlag = true;
    }
    if (totalSeverity3COpenIssues > 0) {
      itemVal += (separatorFlag)?(", " + totalSeverity3COpenIssues + ' SEV 3C Issues '):totalSeverity3COpenIssues + ' SEV 3C Issues ';
      separatorFlag = true;
    }
    if (totalSeverity3DOpenIssues > 0) {
      itemVal += (separatorFlag)?(", " + totalSeverity3DOpenIssues + ' SEV 3C Issues '):totalSeverity3DOpenIssues + ' SEV 3D Issues ';
    }

    return {totalCount: totalOpenIssues, item: itemVal};
  }

  protected count(seriesElement: MetricTimeSeriesElement): number { return 0;}
}
