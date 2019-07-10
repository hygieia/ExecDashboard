import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class TestSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
   
    const rows = [
      {name: 'Integration/Functional (SOAP UI)', order: 1, accurateName: 'soap'},
      {name: 'Integration/Functional (SELENIUM)', order: 2, accurateName: 'sel'},
      {name: 'Integration/Functional (CUCUMBER)', order: 3, accurateName: 'cuc'},
      {name: 'Integration/Functional (HTML)', order: 4, accurateName: 'html'},
      {name: 'Load/Performance (JMeter)', order: 5, accurateName: 'jmeter'}
    ];

    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: [
          {
            name: 'Skipped',
            order: 4,
            value: getData(row.accurateName+"TotalCount")
        },
        {
          name: 'Failed',
          order: 3,
          value: getData(row.accurateName+"SuccessCount")
        },{
          name: 'Succesful',
          order: 2,
          value: getData(row.accurateName+"FailCount") 
        },{
          name: 'Test Cases',
          order: 1,
          value: getData(row.accurateName+"SkipCount")
        }
        ]
      }))
    };


    function getData(label: any): number{
      let num = Number(model.counts[0].label[label])
     
      return num;
         
    }

  }
}
