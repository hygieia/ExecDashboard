import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {UnitTestCoverageDetailStrategy} from '../../strategies/unit-test-coverage-detail-strategy';
import {UnitTestCoverageService} from '../../services/unit-test-coverage.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {UnitTestCoverageBuildingBlocksStrategy} from '../../strategies/unit-test-coverage-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {UnitTestCoverageConfiguration} from '../../unit-test-coverage.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-unit-test-coverage-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    UnitTestCoverageService,
    ProductService
  ]
})
export class UnitTestCoverageDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private testAutomationService: UnitTestCoverageService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: UnitTestCoverageBuildingBlocksStrategy,
              public detailStrategy: UnitTestCoverageDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || UnitTestCoverageConfiguration.detailHeading;
    this.dataService = this.testAutomationService;

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
      primaryText: this.heading || UnitTestCoverageConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
