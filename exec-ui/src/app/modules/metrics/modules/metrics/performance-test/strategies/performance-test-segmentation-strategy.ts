import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class PerformanceTestSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
    parse(model: MetricSummary): MetricSegmentationModel {

        const rows = [
            {name: 'TPS', order: 1},
            {name: 'Response Time', order: 2},
            {name: 'Error Rate', order: 3}

        ];

        return {
            rows: rows.map(row => ({
                name: row.name,
                order: row.order,
                columns: [{
                    name: null,
                    order: null,
                    value: segmentCount(row.name)
                }]
            }))
        };


        function segmentCount(label: string) {
            return model.counts
                .filter(i => i.label['type'] === label)
                .map(c => c.value)
                .reduce((a, b) => a + b, 0);
        }
    }
}
