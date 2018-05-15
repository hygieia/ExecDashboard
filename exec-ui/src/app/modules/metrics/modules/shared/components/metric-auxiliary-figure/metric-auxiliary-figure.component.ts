import {Component, Input} from '@angular/core';
import {MetricValueModel} from '../../component-models/metric-value-model';

@Component({
  selector: 'app-metric-auxiliary-figure',
  templateUrl: './metric-auxiliary-figure.component.html',
  styleUrls: ['./metric-auxiliary-figure.component.scss']
})
export class MetricAuxiliaryFigureComponent {
  @Input() public model: MetricValueModel = null;

  constructor() { }
}
