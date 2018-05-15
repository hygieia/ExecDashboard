import { MetricSummary } from './metric-summary';
export class BuildingBlockMetricSummary {
  id: string;
  name: string;
  commonName: string;
  dashboardDisplayName: string;
  lob: string;
  itemType: string;
  poc: string;
  url: string;
  totalExpectedMetrics: number;
  totalComponents: number;
  reportingComponents: number;
  metrics: MetricSummary[];
}
