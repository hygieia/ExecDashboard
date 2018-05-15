import { MetricTimeSeriesElement } from './metric-time-series-element';
import { MetricSummary } from './metric-summary';
export class MetricDetail {
  summary: MetricSummary;
  timeSeries: MetricTimeSeriesElement[];
  reportingComponents: number;
  totalComponents: number;
}
