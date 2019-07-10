import { Strategy } from '../../../../../shared/strategies/strategy';
import { MetricSummary } from '../../../shared/domain-models/metric-summary';
import { MetricSegmentationModel } from '../../../shared/component-models/metric-segmentation-model';
import { Injectable } from '@angular/core';

@Injectable()
export class DeploySegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {

    const prodDeploys = Number(model.counts[0].label['prodDeploys']);
    const nonProdDeploys = Number(model.counts[0].label['nonProdDeploys']);
    const totalDeploys = Number(model.counts[0].value);
    const successRate = Number(model.counts[0].label['successRate']);

    const rows = [
      { name: 'Prod Deploys', order: 1, value: prodDeploys, unit: '' },
      { name: 'Non Prod Deploys', order: 2, value: nonProdDeploys, unit: '' },
      { name: 'Total Deploys', order: 3, value: totalDeploys, unit: '' },
      { name: 'Success Rate', order: 4, value: successRate, unit: '%' }

    ];


    return {
      rows: rows.map(row => {

        return {
          name: row.name,
          order: row.order,
          unit: row.unit,
          columns: [{
            name: null,
            order: null,
            value: row.value
          }]
        };
      })
    };

    /*
    let data = [];
  
    
  
           data.push({
            name: rows[0].name,
            order: rows[0].order,
            columns: [{
              name: '',
              order: 1,
              value: Number(model.counts[0].label['prodDeploys'])
            }]
          })
  
           data.push({
            name: rows[1].name,
            order: rows[1].order,
            columns: [{
              name: '',
              order: 1,
              value: Number(model.counts[0].label['nonProdDeploys'])
            }]
          })
  
          data.push({
            name: rows[2].name,
            order: rows[2].order,
            columns: [{
              name: '',
              order: 1,
              value: Number(model.counts[0].value),
              unit: '%'
            }]
          })
  
      data.push({
            name: rows[3].name,
            order: rows[3].order,
            columns: [{
              name: '',
              order: 1,
              value: Number(model.counts[0].label['successRate']),
              unit: '%'
            }]
          })
          
      return { 'rows': data};
      */

  }
}
