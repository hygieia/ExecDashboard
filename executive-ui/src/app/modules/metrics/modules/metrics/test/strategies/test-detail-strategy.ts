import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {TestTrendStrategy} from './test-trend-strategy';
import {TestGraphStrategy} from './test-graph-strategy';
import {TestSegmentationStrategy} from './test-segmentation-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {TestAuxiliaryFigureStrategy} from './test-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';

@Injectable()
export class TestDetailStrategy extends DetailStrategyBase {

  constructor (private auxiliaryFigureStrategy: TestAuxiliaryFigureStrategy,
               private trendStrategy: TestTrendStrategy,
               private graphStrategy: TestGraphStrategy,
               private segmentationStrategy: TestSegmentationStrategy) { super(); }

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
      const buildFigure = this.auxiliaryFigureStrategy.parse(summary);

      return {
        hasData: buildFigure.hasData,
        name: buildFigure.name,
        prefix: buildFigure.prefix,
        unit: buildFigure.hasData ? unit(buildFigure) : buildFigure.unit,
        value: buildFigure.value
      };

      function unit(build) {
        if (build.unit === 'days' && build.value > 1) { return 'Days'; }
        if (build.unit === 'days' && build.value === 1) { return 'Day'; }
        if (build.unit === 'hours' && build.value > 1) { return 'Hours'; }
        if (build.unit === 'hours' && build.value === 1) { return 'Hour'; }
        if (build.unit === 'minutes' && build.value === 1) { return 'Minute'; }
        if (build.unit === 'd'&& build.value === 1){ return 'Day'; }
        if (build.unit === 'd'&& build.value > 1){ return 'Days'; }
        if ((build.unit === 'h' || build.unit === 'hr' || build.unit === 'hrs') && build.value === 1){ return 'Hour'; }
        if ((build.unit === 'h' || build.unit === 'hr' || build.unit === 'hrs') && build.value > 1){ return 'Hours'; }
        if (build.unit === 'm'&& build.value === 1){ return 'Minute'; }
        if (build.unit === 'm'&& build.value > 1){ return 'Minutes'; }
        return 'Minutes';
      }
    }
}
