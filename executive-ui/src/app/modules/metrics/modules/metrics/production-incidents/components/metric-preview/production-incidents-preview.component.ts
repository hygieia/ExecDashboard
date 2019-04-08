import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductionIncidentsService } from '../../services/production-incidents.service';
import { ProductionIncidentsPreviewStrategy } from '../../strategies/production-incidents-preview-strategy';
import { ProductionIncidentsConfiguration } from '../../production-incidents.configuration';

/**
 * @export
 * @class ProductionIncidentsPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-production-incidents-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [ProductionIncidentsService]
})
export class ProductionIncidentsPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private productionIncidentsService: ProductionIncidentsService,
    protected router: Router,
    public strategy: ProductionIncidentsPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.productionIncidentsService;
    this.metric = ProductionIncidentsConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
