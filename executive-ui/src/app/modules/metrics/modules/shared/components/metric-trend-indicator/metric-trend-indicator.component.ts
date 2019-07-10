import { Component, OnInit, Input } from '@angular/core';
import {MetricTrendModel} from '../../component-models/metric-trend-model';

@Component({
  selector: 'app-metric-trend-indicator',
  templateUrl: './metric-trend-indicator.component.html',
  styleUrls: ['./metric-trend-indicator.component.scss'],
  providers: []
})
export class MetricTrendIndicatorComponent implements OnInit {
  @Input() trend: MetricTrendModel;

  constructor() { }

  ngOnInit() { }

}
