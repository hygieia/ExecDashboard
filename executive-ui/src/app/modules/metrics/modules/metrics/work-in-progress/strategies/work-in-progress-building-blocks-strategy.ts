import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {Injectable} from '@angular/core';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {WorkInProgressPrimaryMetricStrategy} from './work-in-progress-primary-metric-strategy';
import {WorkInProgressTrendStrategy} from './work-in-progress-trend-strategy';
import {WorkInProgressConfiguration} from '../work-in-progress.configuration';


@Injectable()
export class WorkInProgressBuildingBlocksStrategy extends BuildingBlocksStrategyBase {


constructor (private trendStrategy: WorkInProgressTrendStrategy,
               private primaryMetricStrategy:WorkInProgressPrimaryMetricStrategy) { super(); }


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
        metrics: this.mapWorkInProgressMetric(p),
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

  private mapWorkInProgressMetric(productMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return productMetricSummary.metrics
      .filter((metric) => {
        return metric.name === WorkInProgressConfiguration.identifier;
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
