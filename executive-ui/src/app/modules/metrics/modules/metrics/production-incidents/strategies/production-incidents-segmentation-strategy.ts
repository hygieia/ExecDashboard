import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { MttrModel } from '../../../shared/component-models/mttr-model';
import { OperationalMetricsModel } from '../../../shared/component-models/operational-metrics-model';
import { Injectable } from '@angular/core';

@Injectable()
export class ProductionIncidentsSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
    const rows = [
      { name: '1', order: 1, rowname: 'sev 1' },
      { name: '2', order: 2, rowname: 'sev 2' },
      { name: 'MTBF', order: 3, rowname: 'MTBF' }

    ];

    return {
      rows: rows.map(row => {
        const count = countForSeverity(row.name);
        const impactedApps = countForImpactedApps(row.name);

        if (!checkForImpactedApps(row.name)) {
          if ('MTBF' === row.name) {
            return {
              name: row.rowname,
              order: row.order,
              unit: ' d',
              columns: [{
                name: 'Total',
                order: 1,
                value: count.mtbf
              }]
            };
          } else {
            return {
              name: row.rowname,
              order: row.order,
              columns: [{
                name: 'Total',
                order: 1,
                value: count.closed
              }]
            };
          }
        } else {
          if ('MTBF' === row.name) {
            return {
              name: row.rowname,
              order: row.order,
              unit: ' d',
              columns: [{
                name: 'Total',
                order: 1,
                value: count.mtbf
              }]
            };
          } else {
            return {
              name: row.rowname,
              order: row.order,
              columns: [{
                name: 'Total',
                order: 1,
                value: impactedApps.closed
              }, {
                name: 'Impacted Applications',
                order: 2,
                value: count.closed
              }]
            };
          }

        }
      })
    };

    function countForSeverity(severity: string) {
      const severityCounts = model.counts
        .filter(i => i.label['severity'] === severity);

      const mtbfCounts = model.counts
        .filter(i => i.label['type'] === severity);

      const open = 0;

      const closed = severityCounts
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      const mtbf = mtbfCounts
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);

      return {
        closed: closed,
        mtbf: mtbf
      };
    }

    function countForImpactedApps(severity: string) {
      const severityCounts = model.counts
        .filter(i => i.label['severity'] === severity);

      const open = 0;
      const closed = severityCounts
        .map(c => c.label['impactedApps'])
        .reduce((a, b) => a + b, 0);

      return {
        closed: closed
      };
    }

    function checkForImpactedApps(severity: string) {
      const severityCounts = model.counts
        .filter(i => i.label['severity'] === severity);

      const open = 0;
      const closed = severityCounts
        .map(c => c.label['impactedApps'])
        .reduce((a, b) => a + b, 0);

      if (closed >= 0) {
        return true;
      }



      return false;
    }

  }

  parseOutageDetails(model: MetricSummary): MttrModel[] {
    const outageDetailsMap = model.counts
      .filter(i => i.labelMttr !== undefined && i.label['severity'] === '1');
    return outageDetailsMap[0].labelMttr;
  }

  parseEventDetails(model: MetricSummary): MttrModel[] {
    const eventDetailsMap = model.counts
      .filter(i => i.labelMttr !== undefined && i.label['severity'] === '2');
    return eventDetailsMap[0].labelMttr;
  }

  parseOperationalMetrics(model: MetricSummary): OperationalMetricsModel[] {
    const operationalMetricsMap = model.counts
      .filter(i => i.operationMetrics !== undefined);
    return operationalMetricsMap[0].operationMetrics;
  }

}
