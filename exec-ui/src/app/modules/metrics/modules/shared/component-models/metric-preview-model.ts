import {MetricValueModel} from './metric-value-model';
import {MetricTrendModel} from './metric-trend-model';

export class MetricPreviewModel {
  isRatio: boolean;
  score: MetricValueModel;
  id: string;
  description: string;
  lastScanned: string;
  totalReporting: number;
  hasReporting: number;
  trend: MetricTrendModel;
  secondaryMetrics: MetricValueModel[];
}
