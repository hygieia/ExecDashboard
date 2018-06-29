import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class ProductionIncidentsSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
    const rows = [
      {name: '1', order: 1},
      {name: '2', order: 2},
      {name: '3', order: 3},
      {name: '3C', order: 4},
      {name: '3D', order: 5}
    ];

    return {
      rows: rows.map(row => {
        const count = countForSeverity(row.name);

        return {
          name: row.name,
          order: row.order,
          columns: [{
            name: 'open',
            order: 1,
            value: count.open
          }, {
            name: 'closed',
            order: 2,
            value: count.closed
          }]
        };
      })
    };

    function countForSeverity(severity: string) {
      const severityCounts = model.counts
        .filter(i => i.label['type'] === 'issue')
        .filter(i => i.label['severity'] === severity);

      const open = severityCounts
        .filter(i =>
          (!!i.label['event'].length)
          && ((i.label['event'].toLowerCase() === 'open')
              || (i.label['event'].toLowerCase() === 'opened')))
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      const closed = severityCounts
        .filter(i =>
          (!!i.label['event'].length)
          && ((i.label['event'].toLowerCase() === 'close')
              || (i.label['event'].toLowerCase() === 'closed')))
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      return {
        open: open,
        closed: closed
      };
    }
  }
}
