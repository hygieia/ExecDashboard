import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {ProductionIncidentsTrendStrategy} from './production-incidents-trend-strategy';
import {ProductionIncidentsGraphStrategy} from './production-incidents-graph-strategy';
import {ProductionIncidentsSegmentationStrategy} from './production-incidents-segmentation-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {ProductionIncidentsAuxiliaryFigureStrategy} from './production-incidents-auxiliary-figure-strategy';
import {Injectable} from '@angular/core';

@Injectable()
export class ProductionIncidentsDetailStrategy extends DetailStrategyBase {

  constructor (private auxiliaryFigureStrategy: ProductionIncidentsAuxiliaryFigureStrategy,
               private trendStrategy: ProductionIncidentsTrendStrategy,
               private graphStrategy: ProductionIncidentsGraphStrategy,
               private segmentationStrategy: ProductionIncidentsSegmentationStrategy) { super(); }

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
      const mttrFigure = this.auxiliaryFigureStrategy.parse(summary);

      return {
        hasData: mttrFigure.hasData,
        name: mttrFigure.name,
        prefix: mttrFigure.prefix,
        unit: mttrFigure.hasData ? unit(mttrFigure) : mttrFigure.unit,
        value: mttrFigure.value
      };

      function unit(mttr) {
        if (mttr.unit === 'days' && mttr.value > 1) { return 'Days'; }
        if (mttr.unit === 'days' && mttr.value === 1) { return 'Day'; }
        if (mttr.unit === 'hours' && mttr.value > 1) { return 'Hours'; }
        if (mttr.unit === 'hours' && mttr.value === 1) { return 'Hour'; }
        if (mttr.unit === 'minutes' && mttr.value === 1) { return 'Minute'; }
        return 'Minutes';
      }
    }
}
