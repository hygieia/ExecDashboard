import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class TraceabilitySegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {

    const rows = [
      {name: 'Automated', order: 1},
        {name: 'Manual', order: 2}

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
