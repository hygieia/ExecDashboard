import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {SayDoRatioPrimaryMetricStrategy} from './saydoratio-primary-metric-strategy';
import {SayDoRatioTrendStrategy} from './saydoratio-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {SayDoRatioAuxiliaryFigureStrategy} from './saydoratio-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';
import {SayDoRatioConfiguration} from '../saydoratio.configuration';

@Injectable()
export class SayDoRatioPreviewStrategy extends PreviewStrategyBase {

  constructor (private auxiliaryFigureStrategy: SayDoRatioAuxiliaryFigureStrategy,
              private primaryMetricStrategy: SayDoRatioPrimaryMetricStrategy,
              private trendStrategy: SayDoRatioTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = SayDoRatioConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = SayDoRatioConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.dataSource = SayDoRatioConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: SayDoRatioConfiguration.previewHeading,
        value: valueModel.value,
        unit: '%',
        unitKey: 'Story Points Completed'
      };
    }

     
  }

  private calculateSecondaryMetric(model: MetricSummary) {
   
    const sayDoDisplay = [{
      name: 'Stories Completed',
      value: formattedData(Number(model.counts[0].label["totalStories"]) != 0 ? ((Number(model.counts[0].label["completedStories"])*100)/Number(model.counts[0].label["totalStories"])).toFixed(2): 0.00), 
      unit: "%"
    }
  ];

  function formattedData(value){
    if (value > 0.9) {
      return Math.round(value);
    }else if(value < 0.9 && value > 0.0)
    {
      return value.toFixed(1);
    }else if(value < 0.9 && value > 0.00)
    {
      return value.toFixed(2);
    }else{
      return Math.round(value)
    }
   
  }  
    return [...sayDoDisplay];
   
  }
}

function unit(valueModel):string {
      switch (valueModel.unit) {
        case 'days': return "d";
        case 'hours': return valueModel.value === 1 ? "hr" : "hrs";
        case 'minutes': return "m";
      }
    }