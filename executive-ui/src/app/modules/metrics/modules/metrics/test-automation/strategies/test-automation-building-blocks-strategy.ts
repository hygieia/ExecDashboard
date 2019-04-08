import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {TestAutomationTrendStrategy} from './test-automation-trend-strategy';
import {TestAutomationPrimaryMetricStrategy} from './test-automation-primary-metric-strategy';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {TestAutomationConfiguration} from '../test-automation.configuration';

@Injectable()
export class TestAutomationBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private primaryMetricStrategy: TestAutomationPrimaryMetricStrategy,
               private trendStrategy: TestAutomationTrendStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlocks = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlocks.push({
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
        metrics: this.mapTestAutomationMetric(p),
        detail: this.mapNavigationModel(p),
        appCriticality: p.appCriticality
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

  private mapTestAutomationMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return buildingBlockMetricSummary.metrics
      .filter((metric) => {
        return metric.name === TestAutomationConfiguration.identifier;
      })
      .map((metric) => {
        return {
          name: TestAutomationConfiguration.buildingBlockLabel,
          unit: null,
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: true,
          available: true,
          message: ''
        };
      });
  }
}
