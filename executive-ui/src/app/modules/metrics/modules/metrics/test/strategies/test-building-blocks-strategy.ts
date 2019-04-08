import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {TestPrimaryMetricStrategy} from './test-primary-metric-strategy';
import {TestTrendStrategy} from './test-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {TestAuxiliaryFigureStrategy} from './test-auxiliary-figure-strategy';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {Injectable} from '@angular/core';
import {TestConfiguration} from '../test.configuration';

@Injectable()
export class TestBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private auxiliaryFigureStrategy: TestAuxiliaryFigureStrategy,
               private trendStrategy: TestTrendStrategy,
               private primaryMetricStrategy: TestPrimaryMetricStrategy) { super(); }

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
        metrics: this.mapBuildMetric(p),
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

  private mapBuildMetric(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel[] {
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === TestConfiguration.identifier);
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
          value: { name: TestConfiguration.auxilliaryIdentifier, value: null },
          trend: null,
          available: true,
          message: ''
        };
      }
    }

    function mapSecondary(valueModel) {
      return {
        name: TestConfiguration.auxilliaryIdentifier,
        prefix: '',
        value: valueModel.value,
        unit: valueModel.unit
      };
    }

    function mapSprintName(sprintName) {
      return {
        name: sprintName,
        prefix: '',
        value: null,
        unit: ''
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
