import { BuildingBlockMetricSummaryModel } from './building-block-metric-summary-model';
import {NavigationModel} from '../../../../shared/component-models/navigation-model';

export class BuildingBlockModel {
  id: string;
  name: string;
  lob: string;
  poc: string;
  projectKey: string;
  vastId: string;
  total: number;
  reporting: number;
  appId?: string;
  //completeness: number;
  lastScanned: string;
  metrics: BuildingBlockMetricSummaryModel[];
  detail: NavigationModel;
  appCriticality: string;
  metricType?: string;
}
