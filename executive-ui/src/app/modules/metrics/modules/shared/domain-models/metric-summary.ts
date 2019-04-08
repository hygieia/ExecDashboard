import { Count } from './count';

export class MetricSummary {
  counts: Count[];
  lastScanned: Date;
  lastUpdated: Date;
  trendSlope: number;
  totalComponents: number;
  reportingComponents: number;
  name: string;
  dataAvailable: boolean;
  confMessage: string;
  appCriticality?: string;
}
