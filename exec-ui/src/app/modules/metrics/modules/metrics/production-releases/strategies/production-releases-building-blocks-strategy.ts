import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {ProductionReleasesPrimaryMetricStrategy} from './production-releases-primary-metric-strategy';
import {ProductionReleasesTrendStrategy} from './production-releases-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {Injectable} from '@angular/core';
import {ProductionReleasesConfiguration} from '../production-releases.configuration';

@Injectable()
export class ProductionReleasesBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private trendStrategy: ProductionReleasesTrendStrategy,
               private primaryMetricStrategy: ProductionReleasesPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlockCards = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlockCards.push({
        id: p.id,
        name: p.name,
        commonName: p.commonName,
        dashboardDisplayName: p.dashboardDisplayName,
        lob: p.lob,
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
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === ProductionReleasesConfiguration.identifier);
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
