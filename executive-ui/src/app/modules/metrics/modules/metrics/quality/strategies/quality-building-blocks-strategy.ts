import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { QualityPrimaryMetricStrategy } from './quality-primary-metric-strategy';
import { QualityTrendStrategy } from './quality-trend-strategy';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { Injectable } from '@angular/core';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { QualityConfiguration } from '../quality.configuration';

@Injectable()
export class QualityBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor(private primaryMetricStrategy: QualityPrimaryMetricStrategy,
    private trendStrategy: QualityTrendStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const productCards = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      productCards.push({
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
        metrics: this.mapQualityMetric(p),
        detail: this.mapNavigationModel(p),
        appCriticality: p.appCriticality
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

  private mapQualityMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return buildingBlockMetricSummary.metrics
      .filter((metric) => {
        return metric.name === QualityConfiguration.identifier;
      })
      .map((metric) => {
        return {
          name: metric.name,
          unit: null,
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: false,
          available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
          message: metric.confMessage
        };
      });
  }
}
