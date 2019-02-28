import {MetricPreviewModel} from '../../../shared/component-models/metric-preview-model';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {PresentationFunctions} from '../../../shared/utils/presentation-functions';
import {PreviewStrategyBase} from '../../../shared/strategies/preview-strategy-base';
import {Injectable} from '@angular/core';
import {TraceabilityConfiguration} from '../traceability.configuration';
import {MetricDetail} from '../../../shared/domain-models/metric-detail';
import {TraceabilityPrimaryMetricStrategy} from "../../traceability/strategies/traceability-primary-metric-strategy";
import {TraceabilityTrendStrategy} from "../../traceability/strategies/traceability-trend-strategy";

@Injectable()
export class TraceabilityPreviewStrategy extends PreviewStrategyBase {

    constructor(private primaryMetricStrategy: TraceabilityPrimaryMetricStrategy,
                private trendStrategy: TraceabilityTrendStrategy) {
        super();
    }


    public parse(model: MetricDetail): MetricPreviewModel {
        const metricPreview = new MetricPreviewModel();
        metricPreview.isRatio = true;
        metricPreview.description = TraceabilityConfiguration.description;
        metricPreview.lastScanned = PresentationFunctions.calculateLastScannedPresentation(model.summary.lastScanned);
        metricPreview.id = TraceabilityConfiguration.identifier;
        metricPreview.score = mapPrimary(this.primaryMetricStrategy.parse(model.summary.counts));
        metricPreview.totalReporting = model.reportingComponents / model.totalComponents;
        metricPreview.trend = this.trendStrategy.parse(model.summary);
        metricPreview.secondaryMetrics = this.calculateSecondaryMetric(model.summary);
        return metricPreview;

        function mapPrimary(valueModel) {
            return {
                name: TraceabilityConfiguration.previewHeading,
                value: valueModel.value
            };
        }
    }

    private calculateSecondaryMetric(model: MetricSummary) {
        const validSet = new Set(['Automated', 'Manual']);
        const sums = model.counts
            .filter(count => validSet.has(count.label['type']))
            .reduce((runningSums, count) => {
                return runningSums.set(count.label['type'], count.value);
            }, new Map());

        let result = [];

        if (sums.get('Automated')) {
            result.push({name: 'automated', value: Math.round(sums.get('Automated').toLocaleString())});
        }

        if (sums.get('Manual')) {
            result.push({name: 'manual', value: Math.round(sums.get('Manual').toLocaleString())});
        }
        return result;
    }
}
