import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricDetailModel } from '../../../shared/component-models/metric-detail-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { DeployTrendStrategy } from './deploy-trend-strategy';
import { DeployGraphStrategy } from './deploy-graph-strategy';
import { DeploySegmentationStrategy } from './deploy-segmentation-strategy';
import { DetailStrategyBase } from '../../../shared/strategies/detail-strategy-base';
import { DeployAuxiliaryFigureStrategy } from './deploy-auxiliary-figure-strategy';
import { Injectable } from '@angular/core';

@Injectable()
export class DeployDetailStrategy extends DetailStrategyBase {

  constructor(private auxiliaryFigureStrategy: DeployAuxiliaryFigureStrategy,
    private trendStrategy: DeployTrendStrategy,
    private graphStrategy: DeployGraphStrategy,
    private segmentationStrategy: DeploySegmentationStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.summary.reportingComponents / model.summary.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
    metricDetailView.secondaryFigureModel = this.secondaryFigureModel(model.summary);

    return Object.assign(metricDetailView);

  }

  private secondaryFigureModel(summary: MetricSummary) {
    const deployFigure = this.auxiliaryFigureStrategy.parse(summary);

    return {
      hasData: deployFigure.hasData,
      name: deployFigure.name,
      prefix: deployFigure.prefix,
      unit: deployFigure.hasData ? unit(deployFigure) : deployFigure.unit,
      value: deployFigure.value
    };

    function unit(deploy) {


      if (deploy.unit === 'days' && deploy.value > 1) { return 'Days'; }
      if (deploy.unit === 'days' && deploy.value === 1) { return 'Day'; }
      if (deploy.unit === 'hours' && deploy.value > 1) { return 'Hours'; }
      if (deploy.unit === 'hours' && deploy.value === 1) { return 'Hour'; }
      if (deploy.unit === 'minutes' && deploy.value === 1) { return 'Minute'; }
      if (deploy.unit === 'd' && deploy.value === 1) { return 'Day'; }
      if (deploy.unit === 'd' && deploy.value > 1) { return 'Days'; }
      if ((deploy.unit === 'h' || deploy.unit === 'hr' || deploy.unit === 'hrs') && deploy.value === 1) { return 'Hour'; }
      if ((deploy.unit === 'h' || deploy.unit === 'hr' || deploy.unit === 'hrs') && deploy.value > 1) { return 'Hours'; }
      if (deploy.unit === 'm' && deploy.value === 1) { return 'Minute'; }
      if (deploy.unit === 'm' && deploy.value > 1) { return 'Minutes'; }
      return 'Minutes';
    }
  }
}
