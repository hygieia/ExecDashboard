import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProductionReleasesService } from '../../services/production-releases.service';
import { ProductionReleasesPreviewStrategy } from '../../strategies/production-releases-preview-strategy';
import { ProductionReleasesConfiguration } from '../../production-releases.configuration';

/**
 * @export
 * @class ProductionReleasesPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-production-releases-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [ProductionReleasesService]
})
export class ProductionReleasesPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private productionReleasesService: ProductionReleasesService,
    protected router: Router,
    public strategy: ProductionReleasesPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.productionReleasesService;
    this.metric = ProductionReleasesConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
