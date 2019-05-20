import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class OpenSourceViolationsSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
    const columns = [
      {name: 'license', order: 1},
      {name: 'security', order: 2}
    ];

    const rows = [
        {name: 'Critical', order: 1},
        {name: 'High', order: 2},
        {name: 'Medium', order: 3},
        {name: 'Low', order: 4}
    ];

    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: columns.map(column => ({
          name: column.name,
          order: column.order,
          value: segmentCount(column.name, row.name)
        }))
      }))
    };


    function segmentCount(type: string, severity: string) {
      return model.counts
        .filter(i => i.label['type'] === type && i.label['severity'] === severity)
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);
    }
  }
}
