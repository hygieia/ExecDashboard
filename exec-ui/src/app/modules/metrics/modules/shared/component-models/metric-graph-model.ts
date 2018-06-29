import {MetricTrendModel} from './metric-trend-model';
import {MetricValueModel} from './metric-value-model';

export class MetricGraphModel {
  isRatio: boolean;
  score: MetricValueModel;
  values: number[];
  lastScanned: string;
  trend: MetricTrendModel;
  valueLabel: string;
  toolTipLabel: (number) => string;
  additionalValues: { totalCount: number, item: string}[];
}
