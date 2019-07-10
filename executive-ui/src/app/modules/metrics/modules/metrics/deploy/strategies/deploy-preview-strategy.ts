import { MetricPreviewModel } from '../../../shared/component-models/metric-preview-model';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { DeployPrimaryMetricStrategy } from './deploy-primary-metric-strategy';
import { DeployTrendStrategy } from './deploy-trend-strategy';
import { PreviewStrategyBase } from '../../../shared/strategies/preview-strategy-base';
import { DeployAuxiliaryFigureStrategy } from './deploy-auxiliary-figure-strategy';
import { Injectable } from '@angular/core';
import { DeployConfiguration } from '../deploy.configuration';

@Injectable()
export class DeployPreviewStrategy extends PreviewStrategyBase {

  constructor(private auxiliaryFigureStrategy: DeployAuxiliaryFigureStrategy,
    private primaryMetricStrategy: DeployPrimaryMetricStrategy,
    private trendStrategy: DeployTrendStrategy) { super(); }

  public parse(model: MetricSummary): MetricPreviewModel {
    const metricPreview = new MetricPreviewModel();
    metricPreview.description = DeployConfiguration.description;
    metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.lastScanned);
    metricPreview.id = DeployConfiguration.identifier;
    metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.counts));
    metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
    metricPreview.trend = this.trendStrategy.parse(model);
    metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model);
    metricPreview.available = model.dataAvailable == undefined ? true : model.dataAvailable;
    metricPreview.dataSource = DeployConfiguration.dataSource;
    return metricPreview;

    function mapPrimary(valueModel) {
      return {
        name: DeployConfiguration.previewHeading,
        value: formattedData(valueModel.value),
        unit: getUnit(valueModel.value),
        unitKey: 'Last 90 days'
      };
    }



    function formattedData(value) {
      if (value >= 1000) {
        return Math.round(value / 1000);
      }
      return Math.round(value);
    }

    function getUnit(value) {
      if (value >= 1000) {
        return 'k';
      }
      return '';
    }
  }




  private calculateSecondaryMetric(model: MetricSummary) {

    const totalDeploys = model.counts
      .reduce((sum, count) => sum + count.value, 0);

    const deploy = this.auxiliaryFigureStrategy.parse(model);

    const deployDisplay = [{
      name: 'Frequency Per Day',
      value: Math.round(model.counts[0].label['frequency']),
      unit: ''
    }, {
      name: 'Prod Deploys',
      value: model.counts[0].label['prodDeploys'],
      unit: ''
    }];

    return [...deployDisplay];


  }
}

function unit(valueModel): string {
  switch (valueModel.unit) {
    case 'days': return 'd';
    case 'hours': return valueModel.value === 1 ? 'hr' : 'hrs';
    case 'minutes': return 'm';
  }
}