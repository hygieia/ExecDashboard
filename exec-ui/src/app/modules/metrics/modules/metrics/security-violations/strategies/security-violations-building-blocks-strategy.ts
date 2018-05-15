import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {SecurityViolationsPrimaryMetricStrategy} from './security-violations-primary-metric-strategy';
import {SecurityViolationsTrendStrategy} from './security-violations-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {SecurityViolationsConfiguration} from '../security-violations.configuration';

@Injectable()
export class SecurityViolationsBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private primaryMetricStrategy: SecurityViolationsPrimaryMetricStrategy,
               private trendStrategy: SecurityViolationsTrendStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const productCards = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      productCards.push({
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
        metrics: this.mapSecurityViolationsMetric(p),
        detail: this.mapNavigationModel(p)
      });
    });

    return productCards.sort(this.sortComponents);
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  private mapSecurityViolationsMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return buildingBlockMetricSummary.metrics
      .filter((metric) => {
        return metric.name === SecurityViolationsConfiguration.identifier;
      })
      .map((metric) => {
        return {
          name: metric.name,
          unit: null,
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: false
        };
      });
  }
}
