import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {TraceabilityDetailStrategy} from '../../strategies/traceability-detail-strategy';
import {TraceabilityService} from '../../services/traceability.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {TraceabilityBuildingBlocksStrategy} from '../../strategies/traceability-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {TraceabilityConfiguration} from '../../traceability.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-traceability-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    TraceabilityService,
    ProductService
  ]
})
export class TraceabilityDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private traceabilityService: TraceabilityService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: TraceabilityBuildingBlocksStrategy,
              public detailStrategy: TraceabilityDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || TraceabilityConfiguration.detailHeading;
    this.dataService = this.traceabilityService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioName = params['portfolio-name'];
      this.portfolioLob = params['portfolio-lob'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || TraceabilityConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
