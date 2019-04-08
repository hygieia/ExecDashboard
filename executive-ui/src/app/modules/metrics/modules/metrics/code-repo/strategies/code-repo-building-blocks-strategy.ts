import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { Injectable } from '@angular/core';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { CodeRepoPrimaryMetricStrategy } from './code-repo-primary-metric-strategy';
import { CodeRepoTrendStrategy } from './code-repo-trend-strategy';
import { CodeRepoConfiguration } from '../code-repo.configuration';


@Injectable()
export class CodeRepoBuildingBlocksStrategy extends BuildingBlocksStrategyBase {


  constructor(private trendStrategy: CodeRepoTrendStrategy,
    private primaryMetricStrategy: CodeRepoPrimaryMetricStrategy) { super(); }


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
        lastScanned: this.mapLastScanned(p),
        metrics: this.mapCodeRepoMetric(p),
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

  private mapCodeRepoMetric(productMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return productMetricSummary.metrics
      .filter((metric) => {
        return metric.name === CodeRepoConfiguration.identifier;
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
