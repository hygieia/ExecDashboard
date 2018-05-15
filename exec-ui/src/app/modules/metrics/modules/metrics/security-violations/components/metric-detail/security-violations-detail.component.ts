import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {SecurityViolationsDetailStrategy} from '../../strategies/security-violations-detail-strategy';
import {SecurityViolationsService} from '../../services/security-violations.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {SecurityViolationsBuildingBlocksStrategy} from '../../strategies/security-violations-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {SecurityViolationsConfiguration} from '../../security-violations.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-security-violations-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    SecurityViolationsService,
    ProductService
  ]
})
export class SecurityViolationsDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private securityViolationsService: SecurityViolationsService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: SecurityViolationsBuildingBlocksStrategy,
              public detailStrategy: SecurityViolationsDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || SecurityViolationsConfiguration.detailHeading;
    this.dataService = this.securityViolationsService;

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
      primaryText: this.heading || SecurityViolationsConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
