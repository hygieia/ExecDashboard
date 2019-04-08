import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {TestPrimaryMetricStrategy} from './test-primary-metric-strategy';
import {TestTrendStrategy} from './test-trend-strategy';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {TestAuxiliaryFigureStrategy} from './test-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';
import {TestConfiguration} from '../test.configuration';

@Injectable()
export class TestPreviewStrategy extends PreviewStrategyBase {

  constructor (private auxiliaryFigureStrategy: TestAuxiliaryFigureStrategy,
              private primaryMetricStrategy: TestPrimaryMetricStrategy,
              private trendStrategy: TestTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = TestConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = TestConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.dataSource = TestConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: TestConfiguration.previewHeading,
        value: valueModel.value,
        unit: valueModel.unit,
        unitKey: 'Test Cases'
      };
    }

     
  }

  private calculateSecondaryMetric(model: MetricSummary) {

    let total = model.counts[0].value - Number(model.counts[0].label["cucSkipCount"]) - Number(model.counts[0].label["htmlSkipCount"]) - Number(model.counts[0].label["selSkipCount"]) - Number(model.counts[0].label["soapSkipCount"]) - Number(model.counts[0].label["jmeterSkipCount"]);
    let success = Number(model.counts[0].label["cucSuccessCount"]) + Number(model.counts[0].label["htmlSuccessCount"]) + Number(model.counts[0].label["selSuccessCount"]) + Number(model.counts[0].label["soapSuccessCount"]) + Number(model.counts[0].label["jmeterSuccessCount"]);

    let rate: number = total > 0? Math.round(success*100/total) : 0;

    const sayDoDisplay = [{
      name: 'Success Rate',
      value: rate, 
      unit: "%"
    }
  ];
 
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