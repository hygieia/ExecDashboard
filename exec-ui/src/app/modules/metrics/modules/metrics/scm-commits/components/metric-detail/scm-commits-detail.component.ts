import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {SCMCommitsDetailStrategy} from '../../strategies/scm-commits-detail-strategy';
import {SCMCommitsService} from '../../services/scm-commits.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {SCMCommitsBuildingBlocksStrategy} from '../../strategies/scm-commits-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {SCMCommitsConfiguration} from '../../scm-commits.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-scm-commits-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    SCMCommitsService,
    ProductService
  ]
})
export class SCMCommitsDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private securityViolationsService: SCMCommitsService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: SCMCommitsBuildingBlocksStrategy,
              public detailStrategy: SCMCommitsDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || SCMCommitsConfiguration.detailHeading;
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
      primaryText: this.heading || SCMCommitsConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
