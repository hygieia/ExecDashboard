import { MetricSummary } from './metric-summary';
export class BuildingBlockMetricSummary {
  id: string;
  name: string;
  commonName?: string;
  dashboardDisplayName?: string;
  lob: string;
  poc: string;
  customField?: string;
  url: string;
  metricLevelId: string;
  totalExpectedMetrics: number;
  totalComponents: number;
  reportingComponents?: number;
  completeness?: number;
  metrics: MetricSummary[];
  appCriticality?: string;
  metricType?: string;
  metricLevel: string;
}
