import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { Injectable } from '@angular/core';

@Injectable()
export class DevopscupSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {

    const rows = [
      { name: 'Enginnering ', order: 1, accurateName: 'engg' },
      { name: 'Cloud ', order: 2, accurateName: 'cloud' },
      { name: 'Total ', order: 3, accurateName: 'total' }
    ];

    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: [
          {
            name: 'Improvements',
            order: 2,
            value: Number(segmentCount(row.accurateName == 'total' ? row.accurateName + 'Percent' : row.accurateName + 'Improvements'))
          },
          {
            name: 'Points',
            order: 1,
            value: Number(segmentCount(row.accurateName == 'total' ? row.accurateName + 'Points' : row.accurateName + 'ExcelPoints'))
          }

        ]
      }))
    };


    function segmentCount(type: string) {
      /*  return model.counts
          .filter(i => i.label['type'] === type)
          .map(c => c.value)
          .reduce((a, b) => a + b, 0).toFixed(2);*/
      return 0;
    }
  }
}
