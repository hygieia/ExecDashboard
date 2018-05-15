import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {ProductionReleasesDetailStrategy} from '../../strategies/production-releases-detail-strategy';
import {ProductionReleasesService} from '../../services/production-releases.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {ProductionReleasesBuildingBlocksStrategy} from '../../strategies/production-releases-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {ProductionReleasesConfiguration} from '../../production-releases.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-production-releases-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    ProductionReleasesService,
    ProductService
  ]
})
export class ProductionReleasesDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private securityViolationsService: ProductionReleasesService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: ProductionReleasesBuildingBlocksStrategy,
              public detailStrategy: ProductionReleasesDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || ProductionReleasesConfiguration.detailHeading;
    this.dataService = this.securityViolationsService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioName = params['portfolio-name'];
      this.portfolioLob = params['portfolio-lob'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || ProductionReleasesConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
