import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {StaticCodeAnalysisTrendStrategy} from './static-code-analysis-trend-strategy';
import {StaticCodeAnalysisGraphStrategy} from './static-code-analysis-graph-strategy';
import {StaticCodeAnalysisSegmentationStrategy} from './static-code-analysis-segmentation-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {StaticCodeAnalysisAuxiliaryFigureStrategy} from './static-code-analysis-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';

@Injectable()
export class StaticCodeAnalysisDetailStrategy extends DetailStrategyBase {

  constructor (private trendStrategy: StaticCodeAnalysisTrendStrategy,
               private graphStrategy: StaticCodeAnalysisGraphStrategy,
               private segmentationStrategy: StaticCodeAnalysisSegmentationStrategy,
               private auxiliaryFigureStrategy: StaticCodeAnalysisAuxiliaryFigureStrategy) { super(); }

  public parse(model: MetricDetail): MetricDetailModel {
    const metricDetailView = new MetricDetailModel();
    metricDetailView.issues = model.summary.counts;
    metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
    metricDetailView.totalReporting = model.reportingComponents / model.totalComponents;
    metricDetailView.trend = this.trendStrategy.parse(model.summary);
    metricDetailView.graphModel = this.graphStrategy.parse(model);
    metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);

    metricDetailView.secondaryFigureModel = this.secondaryFigureModel(model.summary);
    return Object.assign(metricDetailView);
  }

  private secondaryFigureModel(summary: MetricSummary) {
    const figure = this.auxiliaryFigureStrategy.parse(summary);

    return {
      hasData: figure.hasData,
      name: figure.name,
      prefix: figure.prefix,
      unit: 'Days',
      value: figure.value
    };
  }
}
