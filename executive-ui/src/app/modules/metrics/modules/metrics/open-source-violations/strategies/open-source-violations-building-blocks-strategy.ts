import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { OpenSourceViolationsTrendStrategy } from './open-source-violations-trend-strategy';
import { OpenSourceViolationsPrimaryMetricStrategy } from './open-source-violations-primary-metric-strategy';
import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { Injectable } from '@angular/core';
import { OpenSourceViolationsConfiguration } from '../open-source-violations.configuration';

@Injectable()
export class OpenSourceViolationsBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor(private trendStrategy: OpenSourceViolationsTrendStrategy,
    private primaryMetricStrategy: OpenSourceViolationsPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const products = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      products.push({
        id: p.id,
        name: p.name,
        lob: p.lob,
        poc: p.poc,
        projectKey: p.customField,
        vastId: p.customField,
        reporting: this.mapReportingComponents(p),
        total: this.mapTotalComponents(p),
        //completeness: this.mapCompleteness(p),
        lastScanned: this.mapLastScanned(p),
        metrics: this.mapOpenSourceViolationsMetric(p),
        detail: this.mapNavigationModel(p),
        appCriticality: p.appCriticality
      });
    });

    return products.sort(this.sortComponents);
  }

  private mapNavigationModel(buildingBlockMetricSummary: BuildingBlockMetricSummary): NavigationModel {
    const navigationModel = new NavigationModel;
    navigationModel.commands = ['product', buildingBlockMetricSummary.id];
    navigationModel.url = buildingBlockMetricSummary.url;
    return navigationModel;
  }

  private mapOpenSourceViolationsMetric(productMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return productMetricSummary.metrics
      .filter((metric) => {
        return metric.name === OpenSourceViolationsConfiguration.identifier;
      })
      .map((metric) => {
        return {
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: false,
          available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
          message: metric.confMessage
        };
      });
  }
}
