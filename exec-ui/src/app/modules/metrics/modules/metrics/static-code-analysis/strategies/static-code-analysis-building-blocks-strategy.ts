import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {StaticCodeAnalysisPrimaryMetricStrategy} from './static-code-analysis-primary-metric-strategy';
import {StaticCodeAnalysisTrendStrategy} from './static-code-analysis-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {StaticCodeAnalysisAuxiliaryFigureStrategy} from './static-code-analysis-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {StaticCodeAnalysisConfiguration} from '../static-code-analysis.configuration';

@Injectable()
export class StaticCodeAnalysisBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private auxiliaryFigureStrategy: StaticCodeAnalysisAuxiliaryFigureStrategy,
               private primaryMetricStrategy: StaticCodeAnalysisPrimaryMetricStrategy,
               private trendStrategy: StaticCodeAnalysisTrendStrategy) { super(); }

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
        metrics: this.mapStaticCodeAnalysisMetric(p),
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

  private mapStaticCodeAnalysisMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === StaticCodeAnalysisConfiguration.identifier);

    if (!metric) {
      return [];
    }

    return [
      {
        value: this.primaryMetricStrategy.parse(metric.counts),
        trend: this.trendStrategy.parse(metric),
        isRatio: false
      },
      mapSecondaryMetric(this.auxiliaryFigureStrategy.parse(metric))
    ];

    function mapSecondaryMetric(value) {
      if (value.hasData) {
        return {
          value: mapSecondary(value),
          trend: null,
          isRatio: false
        };
      } else {
        return {
          isEmpty: true,
          isRatio: false,
          value: { name: StaticCodeAnalysisConfiguration.auxilliaryIdentifier, value: null },
          trend: null
        };
      }
    }

    function mapSecondary(valueModel) {
      return {
        prefix: '~',
        name: StaticCodeAnalysisConfiguration.auxilliaryIdentifier,
        unit: 'd',
        value: valueModel.value
      };
    }
  }
}
