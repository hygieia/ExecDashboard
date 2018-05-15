import {Component, Input, OnChanges} from '@angular/core';
import {MetricSegmentationModel} from '../../component-models/metric-segmentation-model';

@Component({
  selector: 'app-metric-segmentation',
  templateUrl: 'metric-segmentation.component.html',
  styleUrls: ['metric-segmentation.component.scss']
})
export class MetricSegmentationComponent implements OnChanges {
  @Input() public model: MetricSegmentationModel = null;

  constructor() {
  }

  columnLabels() {
    if (!this.model) {
      return [];
    }
    const labels = this.model.rows[0].columns
      .sort((a, b) => b.order - a.order)
      .map(x => x.name);

    return labels[0] !== null ? labels : [];
  }

  rowValues() {
    if (!this.model) {
      return [];
    }
    return this.model.rows
      .sort((a, b) => a.order - b.order)
      .map(row => ({
        label: row.name,
        values: row.columns
          .sort((a, b) => b.order - a.order)
          .map(column => column.value)
      }));
  }

  ngOnChanges() {
    this.columnLabels();
    this.rowValues();
  }
}
