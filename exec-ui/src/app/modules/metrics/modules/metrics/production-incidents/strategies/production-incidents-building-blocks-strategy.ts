import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {ProductionIncidentsPrimaryMetricStrategy} from './production-incidents-primary-metric-strategy';
import {ProductionIncidentsTrendStrategy} from './production-incidents-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {ProductionIncidentsAuxiliaryFigureStrategy} from './production-incidents-auxiliary-figure-strategy';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {Injectable} from '@angular/core';
import {ProductionIncidentsConfiguration} from '../production-incidents.configuration';

@Injectable()
export class ProductionIncidentsBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private auxiliaryFigureStrategy: ProductionIncidentsAuxiliaryFigureStrategy,
               private trendStrategy: ProductionIncidentsTrendStrategy,
               private primaryMetricStrategy: ProductionIncidentsPrimaryMetricStrategy) { super(); }

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
        metrics: this.mapProductionIncidentsMetric(p),
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

  private mapProductionIncidentsMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    if (!buildingBlockMetricSummary.metrics) {
      return [];
    }

    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === ProductionIncidentsConfiguration.identifier);
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
          value: { name: ProductionIncidentsConfiguration.auxilliaryIdentifier, value: null },
          trend: null
        };
      }
    }

    function mapSecondary(valueModel) {
      return {
        name: ProductionIncidentsConfiguration.auxilliaryIdentifier,
        prefix: '~',
        value: valueModel.value,
        unit: unit(valueModel)
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
