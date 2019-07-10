import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { Injectable } from '@angular/core';

@Injectable()
export class QualitySegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {

    const rows = [
      { accurateName: 'cmis', order: 1, name: 'cmis defects' },
      { accurateName: 'normal', order: 4, name: 'P3: Jira' },
      { accurateName: 'blocker', order: 2, name: 'P1: Jira' },
      { accurateName: 'high', order: 3, name: 'P2: Jira' },
      { accurateName: 'low', order: 5, name: 'P4: Jira' },
      { accurateName: 'total', order: 6, name: 'Jira Defects' },
      { accurateName: 'serviceNow', order: 7, name: 'SN Defects' },
      { accurateName: 'serviceNowOne', order: 8, name: 'P1: SN' },
      { accurateName: 'serviceNowTwo', order: 9, name: 'P2: SN' },
      { accurateName: 'serviceNowThree', order: 10, name: 'P3: SN' },
      { accurateName: 'serviceNowFour', order: 11, name: 'P4: SN' }
    ];

    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: [{
          name: 'Total',
          order: 1,
          value: segmentCount('open' + row.accurateName)
        }, {
          name: 'Closed',
          order: 1,
          value: segmentCount('close' + row.accurateName)
        }, {
          name: 'Open',
          order: 1,
          value: segmentCount(row.accurateName)
        }]
      }))
    };


    function segmentCount(priority: string) {
      return model.counts
        .filter(i => i.label['priority'] === priority)
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);
    }
  }
}
