import { MetricDetailModel } from '../../../shared/component-models/metric-detail-model';
import { MetricDetail } from '../../../shared/domain-models/metric-detail';
import { PresentationFunctions } from '../../../shared/utils/presentation-functions';

import { DevopscupGraphStrategy } from './devopscup-graph-strategy';
import { DevopscupSegmentationStrategy } from './devopscup-segmentation-strategy';
import { DevopscupTrendStrategy } from './devopscup-trend-strategy';


import { DetailStrategyBase } from '../../../shared/strategies/detail-strategy-base';
import { Injectable } from '@angular/core';


@Injectable()
export class DevopscupDetailStrategy extends DetailStrategyBase {

  constructor(private graphStrategy: DevopscupGraphStrategy,
    private trendStrategy: DevopscupTrendStrategy,
    private segmentationStrategy: DevopscupSegmentationStrategy) { super() }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation((model.summary.lastScanned == null || model.summary.lastScanned == undefined) ? new Date() : model.summary.lastScanned);
    metricDetailView.totalReporting = model.summary.reportingComponents / model.summary.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
    metricDetailView.isRatio = true;
    return Object.assign(metricDetailView);
  }
}
