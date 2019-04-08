import { BuildingBlockMetricSummaryModel } from '../../../shared/component-models/building-block-metric-summary-model';
import { BuildingBlockMetricSummary } from '../../../shared/domain-models/building-block-metric-summary';
import { DeployPrimaryMetricStrategy } from './deploy-primary-metric-strategy';
import { DeployTrendStrategy } from './deploy-trend-strategy';
import { BuildingBlockModel } from '../../../shared/component-models/building-block-model';
import { BuildingBlocksStrategyBase } from '../../../shared/strategies/building-blocks-strategy-base';
import { DeployAuxiliaryFigureStrategy } from './deploy-auxiliary-figure-strategy';
import { NavigationModel } from '../../../../../shared/component-models/navigation-model';
import { Injectable } from '@angular/core';
import { DeployConfiguration } from '../deploy.configuration';

@Injectable()
export class DeployBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor(private auxiliaryFigureStrategy: DeployAuxiliaryFigureStrategy,
    private trendStrategy: DeployTrendStrategy,
    private primaryMetricStrategy: DeployPrimaryMetricStrategy) { super(); }

  parse(model: BuildingBlockMetricSummary[]): BuildingBlockModel[] {
    const buildingBlockCards = new Array<BuildingBlockModel>();
    model.forEach((p) => {
      buildingBlockCards.push({
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
        metrics: this.mapDeployMetric(p),
        detail: this.mapNavigationModel(p),
        appCriticality: p.appCriticality
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

  private mapDeployMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === DeployConfiguration.identifier);
    if (!metric) {
      return [];
    }

    return [
      {
        value: this.primaryMetricStrategy.parse(metric.counts),
        trend: this.trendStrategy.parse(metric),
        isRatio: false,
        available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
        message: metric.confMessage
      },
      mapSecondaryMetric(this.auxiliaryFigureStrategy.parse(metric))
    ];

    function mapSecondaryMetric(value) {
      if (value.hasData) {
        return {
          value: mapSecondary(value),
          trend: null,
          isRatio: false,
          available: true,
          message: ''
        };
      } else {
        return {
          isEmpty: true,
          isRatio: false,
          value: { name: DeployConfiguration.auxilliaryIdentifier, value: null },
          trend: null,
          available: true,
          message: ''
        };
      }
    }

    function mapSecondary(valueModel) {
      return {
        name: DeployConfiguration.auxilliaryIdentifier,
        prefix: '~',
        value: valueModel.value,
        unit: valueModel.unit
      };
    }

    function unit(valueModel) {
      switch (valueModel.unit) {
        case 'days': return 'd';
        case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
        case 'minutes': return 'm';
      }
    }
  }
}
