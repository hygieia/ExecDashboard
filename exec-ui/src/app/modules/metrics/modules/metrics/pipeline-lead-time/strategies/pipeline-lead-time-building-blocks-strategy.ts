import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {PipelineLeadTimeBuildingBlockPrimaryMetricStrategy} from './pipeline-lead-time-building-block-primary-metric-strategy';
import {PipelineLeadTimeConfiguration} from '../pipeline-lead-time.configuration';

@Injectable()
export class PipelineLeadTimeBuildingBlocksStrategy extends BuildingBlocksStrategyBase {
  constructor (private primaryMetricsStrategy: PipelineLeadTimeBuildingBlockPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlocks = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlocks.push({
        id: p.id,
        name: p.name,
        commonName: p.commonName,
        lob: p.lob,
        dashboardDisplayName: p.dashboardDisplayName,
        poc: p.poc,
        itemType: p.itemType,
        reporting: this.mapReportingComponents(p),
        total: this.mapTotalComponents(p),
        completeness: this.mapCompleteness(p),
        lastScanned: this.mapLastScanned(p),
        metrics: this.mapMetrics(p),
        detail: this.mapNavigationModel(p)
      });
    });

    return buildingBlocks.sort(this.sortComponents);
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  private mapMetrics(buildingBlockMetricSummary: BuildingBlockMetricSummary) {
    return buildingBlockMetricSummary.metrics
      .filter((metric) => {
        return metric.name === PipelineLeadTimeConfiguration.identifier;
      })
      .map((metric) => {
        return this.primaryMetricsStrategy.parse(metric);
      });
  }
}
