import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class SecurityViolationsSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
    const rows = [
      {name: 'critical', order: 1, accurateName: 'Critical'},
      {name: 'high', order: 2, accurateName: 'Blocker'},
      {name: 'medium', order: 3, accurateName: 'Major'}
    ];

    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: [
          {
          name: 'overdue',
          order: 1,
          value: segmentCount('blackDuck'+row.accurateName)
        },{
          name: 'code',
          order: 2,
          value: segmentCount('web'+row.accurateName)
        },{
          name: 'port',
          order: 3,
          value: segmentCount('port'+row.accurateName)
        },{
          name: 'web',
          order: 4,
          value: segmentCount('code'+row.accurateName)
        },{
          name: 'blackDuck',
          order: 5,
          value: segmentCount('overdue'+row.accurateName)
        }

        ]
      }))
    };


    function segmentCount(severity: string) {
      return model.counts
        .filter(i =>  i.label['severity'] === severity)
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);
    }
  }
}