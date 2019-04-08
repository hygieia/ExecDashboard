import {Strategy} from '../../../../../shared/strategies/strategy';
import {MetricSummary} from '../../../shared/domain-models/metric-summary';
import {MetricSegmentationModel} from '../../../shared/component-models/metric-segmentation-model';
import {Injectable} from '@angular/core';

@Injectable()
export class SayDoRatioSegmentationStrategy implements Strategy<MetricSummary, MetricSegmentationModel> {
  parse(model: MetricSummary): MetricSegmentationModel {
   

    const totalBuilds = Number(model.counts[0].label["totalStoryPoints"]);
    const successRate = Number(model.counts[0].label["completedStoryPoints"]);

    const rows = [
      {name: 'Stories', order: 1, accurateName: 'Stories'},
      {name: 'Story Points', order: 2, accurateName: 'StoryPoints'}
    ];




    return {
      rows: rows.map(row => ({
        name: row.name,
        order: row.order,
        columns: [
          {
          name: 'Ratio',
          order: 3,
          value: segmentCount("total"+row.accurateName)
        },{
          name: 'Completed',
          order: 2,
          value: segmentCount("completed"+row.accurateName)
        },{
          name: 'Committed',
          order: 1,
          value: Number(segmentCountForRatio("total"+row.accurateName)) > 0.00 ? checkSegmentation(row) : String(0.00)
        }
        ]
      }))
    };

    function checkSegmentation(row){
     
      let pt = (Number(segmentCountForRatio("completed"+row.accurateName))/Number(segmentCountForRatio("total"+row.accurateName))).toFixed(2)
      if(Number(pt) > 1000){
        return String(Math.round(Number(pt))) + 'k'
      }
      
      return String(pt)
    }

    function segmentCount(label: string) {
      if(label == 'totalStoryPoints' || label == 'completedStoryPoints'){
        let pt =  Math.round(Number(model.counts[0].label[label])).toFixed(2);
        
        if(Number(pt) > 1000.00){
            return String(Math.round(Number(pt)/1000.00)) + 'k'
        }
        return String(pt)
      }
      return model.counts[0].label[label];
    }

    function segmentCountForRatio(label: string) {
     
      return model.counts[0].label[label];
    }



  }
}
