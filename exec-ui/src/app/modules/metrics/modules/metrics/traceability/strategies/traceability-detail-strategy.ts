import {MetricDetailModel} from '../../../shared/component-models/metric-detail-model';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {TraceabilityGraphStrategy} from './traceability-graph-strategy';
import {TraceabilityTrendStrategy} from './traceability-trend-strategy';
import {DetailStrategyBase} from '../../../shared/strategies/detail-strategy-base';
import {Injectable} from '@angular/core';
import {TraceabilitySegmentationStrategy} from "../../traceability/strategies/traceability-segmentation-strategy";

@Injectable()
export class TraceabilityDetailStrategy extends DetailStrategyBase {

    constructor(private graphStrategy: TraceabilityGraphStrategy,
                private trendStrategy: TraceabilityTrendStrategy,
                private segmentationStrategy: TraceabilitySegmentationStrategy) {
        super();
    }

    public parse(model: MetricDetail): MetricDetailModel {
        const metricDetailView = new MetricDetailModel();
        metricDetailView.issues = model.summary.counts;
        metricDetailView.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
        metricDetailView.totalReporting = model.reportingComponents / model.totalComponents;
        metricDetailView.trend = this.trendStrategy.parse(model.summary);
        metricDetailView.graphModel = this.graphStrategy.parse(model);
        metricDetailView.segmentationModel = this.segmentationStrategy.parse(model.summary);
        metricDetailView.isRatio = true;


        return metricDetailView;


    }
}

