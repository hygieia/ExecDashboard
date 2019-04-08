import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricDetailModel } from '../../../shared/component-models/metric-detail-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';
import { CloudTrendStrategy } from './cloud-trend-strategy';
import { CloudGraphStrategy } from './cloud-graph-strategy';
import { CloudSegmentationStrategy } from './cloud-segmentation-strategy';
import { DetailStrategyBase } from '../../../shared/strategies/detail-strategy-base';
import { CloudAuxiliaryFigureStrategy } from './cloud-auxiliary-figure-strategy';
import { Injectable } from '@angular/core';

@Injectable()
export class CloudDetailStrategy extends DetailStrategyBase {

  constructor(private auxiliaryFigureStrategy: CloudAuxiliaryFigureStrategy,
    private trendStrategy: CloudTrendStrategy,
    private graphStrategy: CloudGraphStrategy,
    private segmentationStrategy: CloudSegmentationStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.summary.reportingComponents / model.summary.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
    //    metricDetailView.secondaryFigureModel = this.secondaryFigureModel(model.summary);

    return Object.assign(metricDetailView);

  }

  private secondaryFigureModel(summary: MetricSummary) {
    const cloudFigure = this.auxiliaryFigureStrategy.parse(summary);
    return {
      hasData: cloudFigure.hasData,
      name: cloudFigure.name,
      prefix: cloudFigure.prefix,
      unit: '$',
      value: cloudFigure.value
    };
  }
}

