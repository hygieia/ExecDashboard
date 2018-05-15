import {Strategy} from '../../../../shared/strategies/strategy';
import {MetricGraphModel} from '../component-models/metric-graph-model';
import {MetricDetail} from '../domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../domain-models/metric-time-series-element';

export abstract class GraphStrategyBase implements Strategy<MetricDetail, MetricGraphModel> {
  public abstract parse(model: MetricDetail): MetricGraphModel;

  protected abstract count(seriesElement: MetricTimeSeriesElement): number;

  protected fillInMissingTimeSeriesElements(timeSeries: MetricTimeSeriesElement[]): number[] {
    if (timeSeries.length >= 90) {
      return timeSeries.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
    }

    const timeSeriesPrefilled: MetricTimeSeriesElement[] = [];
    for (let i = 0; i <= 90; i++) {
      const timeSeriesElement: MetricTimeSeriesElement = new MetricTimeSeriesElement();
      timeSeriesElement.daysAgo = i;
      timeSeriesElement.counts = [];
      timeSeriesPrefilled.push(timeSeriesElement);
    }

    const timeSeriesSorted: MetricTimeSeriesElement[] = timeSeries.sort((a, b) => a.daysAgo - b.daysAgo);
    for (let y = 0; y < timeSeriesSorted.length; y++) {
      const timeSeriesPrefilledElement
        = timeSeriesPrefilled.find(x => x.daysAgo === timeSeriesSorted[y].daysAgo);
      if (timeSeriesPrefilledElement) {
        timeSeriesPrefilledElement.counts = timeSeriesSorted[y].counts;
      }
    }

    return timeSeriesPrefilled.sort((a, b) => a.daysAgo - b.daysAgo).map(this.count);
  }
}
