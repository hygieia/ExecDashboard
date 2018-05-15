import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {SCMCommitsPrimaryMetricStrategy} from './scm-commits-primary-metric-strategy';
import {SCMCommitsTrendStrategy} from './scm-commits-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {Injectable} from '@angular/core';
import {SCMCommitsConfiguration} from '../scm-commits.configuration';

@Injectable()
export class SCMCommitsBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private trendStrategy: SCMCommitsTrendStrategy,
               private primaryMetricStrategy: SCMCommitsPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlockCards = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlockCards.push({
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
        metrics: this.mapProductionIncidentsMetric(p),
        detail: this.mapNavigationModel(p)
      });
    });

    return buildingBlockCards.sort(this.sortComponents);
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  private mapProductionIncidentsMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === SCMCommitsConfiguration.identifier);
    if (!metric) {
      return [];
    }

    return [{
      value: this.primaryMetricStrategy.parse(metric.counts),
      trend: this.trendStrategy.parse(metric),
      isRatio: false
    }];
  }
}
