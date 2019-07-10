import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { Injectable } from '@angular/core';

@Injectable()
export class PipelineLeadTimeSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
    const rows = [
      { name: 'Total Commits', order: 1 },
      { name: 'Deployment Cadence', order: 2 }
    ];

    return {
      rows: rows.map(row => {
        const count = countForCommits();

        let value = count.commits;
        let unit = '';
        if (row.order === 2) {
          value = Math.round(count.cadence);
          unit = ' d';
        }
        return {
          name: row.name,
          order: row.order,
          unit: unit,
          columns: [{
            name: '',
            order: 1,
            value: value
          }]
        };
      })
    };

    function countForCommits() {
      let commits = 0;
      let cadence;

      commits = model.counts
        .filter(i => i.label['type'] === "commit")
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      cadence = model.counts
        .filter(i => i.label['type'] === "cadence")
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      if (cadence == "0NaN") {
        cadence = 0;
      }


      return {
        commits: commits,
        cadence: cadence
      };
    }
  }
}
