import {MetricTrendModel} from './metric-trend-model';
import {MetricValueModel} from './metric-value-model';

export class MetricGraphModel {
  isRatio: boolean;
  score: MetricValueModel;
  values:Map<number, number>;
  lastScanned: string;
  trend: MetricTrendModel;
  valueLabel: string;
  toolTipLabel: (number) => string;
}
