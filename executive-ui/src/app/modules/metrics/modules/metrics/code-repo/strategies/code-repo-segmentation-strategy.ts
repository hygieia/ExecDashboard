import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { Injectable } from '@angular/core';

@Injectable()
export class CodeRepoSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {

    const rows = [
      { name: 'Unique Contributors', order: 1, accurateName: 'uniqueContributors' },
      { name: 'Merged PR', order: 1, accurateName: 'mergedPullRequests' },
      { name: 'Declined PR', order: 2, accurateName: 'declinedPullRequests' }
    ];

    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: [{
          name: null,
          order: null,
          value: segmentCount(row.accurateName)
        }]
      }))
    };


    function segmentCount(type: string) {
      return model.counts
        .filter(i => i.label['type'] === type)
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);
    }
  }
}
