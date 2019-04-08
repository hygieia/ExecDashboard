import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { Injectable } from '@angular/core';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { DevopscupPrimaryMetricStrategy } from './devopscup-primary-metric-strategy';
import { DevopscupTrendStrategy } from './devopscup-trend-strategy';
import { DevopsCupConfiguration } from '../devopscup.configuration';
import { DevopscupScores } from '../../../../../shared/domain-models/devopscupScores';


@Injectable()
export class DevopscupBuildingBlocksStrategy extends BuildingBlocksStrategyBase {


  constructor(private trendStrategy: DevopscupTrendStrategy,
    private primaryMetricStrategy: DevopscupPrimaryMetricStrategy) { super(); }


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
        lastScanned: this.mapLastScanned(p) == null || this.mapLastScanned(p) == undefined ? '' : this.mapLastScanned(p),
        metrics: this.mapDevopscupMetric(p),
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

  private mapDevopscupMetric(productMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return productMetricSummary.metrics
      .filter((metric) => {
        return metric.name === DevopsCupConfiguration.identifier;
      })
      .map((metric) => {
        return {
          name: metric.name,
          unit: '',
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: true,
          available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
          message: 'No Reports'
        };
      });
  }
}
