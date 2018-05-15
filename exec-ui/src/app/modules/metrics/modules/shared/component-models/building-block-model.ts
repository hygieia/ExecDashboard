import { BuildingBlockMetricSummaryModel } from './building-block-metric-summary-model';
import {NavigationModel} from '../../../../shared/component-models/navigation-model';

export class BuildingBlockModel {
  id: string;
  name: string;
  commonName: string;
  lob: string;
  dashboardDisplayName: string;
  poc: string;
  itemType: string;
  total: number;
  reporting: number;
  completeness: number;
  lastScanned: string;
  metrics: BuildingBlockMetricSummaryModel[];
  detail: NavigationModel;
}
