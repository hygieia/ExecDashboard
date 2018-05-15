import {Count} from './count';

export class MetricSummary {
  counts: Count[];
  lastScanned: Date;
  lastUpdated: Date;
  trendSlope: number;
  name: string;
}
