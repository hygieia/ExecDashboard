import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {UnitTestCoverageTrendStrategy} from './unit-test-coverage-trend-strategy';
import {UnitTestCoveragePrimaryMetricStrategy} from './unit-test-coverage-primary-metric-strategy';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {UnitTestCoverageConfiguration} from '../unit-test-coverage.configuration';

@Injectable()
export class UnitTestCoverageBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private primaryMetricStrategy: UnitTestCoveragePrimaryMetricStrategy,
               private trendStrategy: UnitTestCoverageTrendStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlocks = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlocks.push({
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
        metrics: this.mapUnitTestCoverageMetric(p),
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

  private mapUnitTestCoverageMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    return buildingBlockMetricSummary.metrics
      .filter((metric) => {
        return metric.name === UnitTestCoverageConfiguration.identifier;
      })
      .map((metric) => {
        return {
          value: this.primaryMetricStrategy.parse(metric.counts),
          trend: this.trendStrategy.parse(metric),
          isRatio: true
        };
      });
  }
}
