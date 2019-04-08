import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class ThroughputSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {

    const rows = [
      {name: 'Total Time', order: 3, accurateName: 'Total Time', unit: 'd'},
      {name: 'User Stories', order: 1, accurateName: 'Total User Stories', unit: ''},
      {name: 'Story Points', order: 2, accurateName: 'Total Story Points', unit: ''}
    ];

     return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        unit: row.unit,
        columns: [
          {
          name: 'Total',
          order: 4,
          value: segmentCount(row.accurateName + ' Three')
        },{
          name: getColumnName(30),
          order: 3,
          value: segmentCount(row.accurateName + ' Two')
        },{
          name: getColumnName(60),
          order: 2,
          value: segmentCount(row.accurateName + ' One')
        },{
          name: getColumnName(90),
          order: 1,
          value: segmentCount(row.accurateName)
        }
        ]
      }))
    };


    function segmentCount(type: string) {
      return model.counts
        .filter(i => i.label['type'] === type)
        .map(c => c.value)
        .reduce((a, b) => a + b, 0);
        
    }

    function getColumnName(interval: number){
      const m = new Date();
      if(30 == interval) {
        m.setDate(m.getDate());
        const date: String = m.toDateString().substring(4);
        const n = new Date();
        n.setDate(n.getDate() - Number(interval));
        const datum: String = n.toDateString().substring(4);
        return  (datum).substring(0, date.length - 4) + '-' + (date).substring(0, date.length - 4);
      }
      else if(60 == interval) {
        m.setDate(m.getDate() - Number(30));
        const date: String = m.toDateString().substring(4);
        const n = new Date();
        n.setDate(n.getDate() - Number(interval));
        const datum: String = n.toDateString().substring(4);
        return (datum).substring(0, date.length - 4) + '-' + (date).substring(0, date.length - 4);
      }
      else if (90 == interval) {
        m.setDate(m.getDate() - Number(60));
        const date: String = m.toDateString().substring(4);
        const n = new Date();
        n.setDate(n.getDate() - Number(interval));
        const datum: String = n.toDateString().substring(4);
        return (datum).substring(0, date.length - 4)  + '-' + (date).substring(0, date.length - 4);
      }
    }
  }
}