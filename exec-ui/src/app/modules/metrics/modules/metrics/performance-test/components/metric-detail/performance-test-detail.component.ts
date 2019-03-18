import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {PerformanceTestDetailStrategy} from '../../strategies/performance-test-detail-strategy';
import {PerformanceTestService} from '../../services/performance-test.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {PerformanceTestBuildingBlocksStrategy} from '../../strategies/performance-test-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {PerformanceTestConfiguration} from '../../performance-test.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-performance-test-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    PerformanceTestService,
    ProductService
  ]
})
export class PerformanceTestDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private performanceTestService: PerformanceTestService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: PerformanceTestBuildingBlocksStrategy,
              public detailStrategy: PerformanceTestDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || PerformanceTestConfiguration.detailHeading;
    this.dataService = this.performanceTestService;

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
      primaryText: this.heading || PerformanceTestConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
