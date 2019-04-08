import {BuildingBlockMetricSummaryModel} from '../../../shared/component-models/building-block-metric-summary-model';
import {BuildingBlockMetricSummary} from '../../../shared/domain-models/building-block-metric-summary';
import {SayDoRatioPrimaryMetricStrategy} from './saydoratio-primary-metric-strategy';
import {SayDoRatioTrendStrategy} from './saydoratio-trend-strategy';
import {BuildingBlockModel} from '../../../shared/component-models/building-block-model';
import {BuildingBlocksStrategyBase} from '../../../shared/strategies/building-blocks-strategy-base';
import {SayDoRatioAuxiliaryFigureStrategy} from './saydoratio-auxiliary-figure-strategy';
import {NavigationModel} from '../../../../../shared/component-models/navigation-model';
import {Injectable} from '@angular/core';
import {SayDoRatioConfiguration} from '../saydoratio.configuration';

@Injectable()
export class SayDoRatioBuildingBlocksStrategy extends BuildingBlocksStrategyBase {

  constructor (private auxiliaryFigureStrategy: SayDoRatioAuxiliaryFigureStrategy,
               private trendStrategy: SayDoRatioTrendStrategy,
               private primaryMetricStrategy: SayDoRatioPrimaryMetricStrategy) { super(); }

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
    const metric = buildingBlockMetricSummary.metrics.find(m => m.name === SayDoRatioConfiguration.identifier);
    if (!metric) {
      return [];
    }

    
    if( mapSprintNameObj(buildingBlockMetricSummary)!=undefined){
    return [
      {
        value: this.primaryMetricStrategy.parse(metric.counts),
        trend: this.trendStrategy.parse(metric),
        isRatio: false,
        available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
        message: metric.confMessage
      },
      mapSecondaryMetric(this.auxiliaryFigureStrategy.parse(metric)),
      mapSprintNameObj(buildingBlockMetricSummary)
    ];
  }else{
    return [
      {
        value: this.primaryMetricStrategy.parse(metric.counts),
        trend: this.trendStrategy.parse(metric),
        isRatio: false,
        available: metric.dataAvailable == undefined ? true : metric.dataAvailable,
        message: metric.confMessage
      },
      mapSecondaryMetric(this.auxiliaryFigureStrategy.parse(metric)),
    ]
    }

    function mapSprintNameObj(buildingBlockMetricSummary: BuildingBlockMetricSummary): BuildingBlockMetricSummaryModel{
     
      if(buildingBlockMetricSummary != undefined && buildingBlockMetricSummary.metrics!= undefined && buildingBlockMetricSummary.metrics.length > 0 && buildingBlockMetricSummary.metrics[0].counts != undefined && buildingBlockMetricSummary.metrics[0].counts.length != 0 && buildingBlockMetricSummary.metrics[0].counts[0].label["sprintName"] != undefined){
       
        return {
          value:  mapSprintName(buildingBlockMetricSummary.metrics[0].counts[0].label["sprintName"]),
          trend: null,
          isRatio: false,
          available: false,
          message: ''
        }
      }else{
        return undefined;
      }
    }

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
          value: { name: SayDoRatioConfiguration.auxilliaryIdentifier, value: null },
          trend: null,
          available: true,
          message: ''
        };
      }
    }

    function mapSecondary(valueModel) {
      return {
        name: SayDoRatioConfiguration.auxilliaryIdentifier,
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
