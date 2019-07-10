import {Strategy} from '../../../../shared/strategies/strategy';
import {MetricGraphModel} from '../component-models/metric-graph-model';
import {MetricDetail} from '../domain-models/metric-detail';
import {MetricTimeSeriesElement} from '../domain-models/metric-time-series-element';

export abstract class GraphStrategyBase implements Strategy<MetricDetail, MetricGraphModel> {
  public abstract parse(model: MetricDetail): MetricGraphModel;

  protected abstract count(seriesElement: MetricTimeSeriesElement): number;
}
