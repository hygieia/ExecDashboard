import { MetricDetailModel } from '../../../../shared/component-models/metric-detail-model';
import { ProductionReleasesDetailStrategy } from '../../strategies/production-releases-detail-strategy';
import { ProductionReleasesService } from '../../services/production-releases.service';
import { Component, OnInit } from '@angular/core';
import { MetricDetailBaseComponent } from '../../../../shared/components/metric-detail/metric-detail-base.component';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { PortfolioService } from '../../../../../../shared/shared.module';
import { AuthService } from '../../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../../services/vz/track.service';
import { ProductionReleasesBuildingBlocksStrategy } from '../../strategies/production-releases-building-blocks-strategy';
import { ProductService } from '../../../../shared/services/product.service';
import { ProductionReleasesConfiguration } from '../../production-releases.configuration';
import { Location } from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-production-releases-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    ProductionReleasesService,
    ProductService,
    Ng4LoadingSpinnerComponent
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
    protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    protected location: Location,
    protected authService: AuthService,
    protected trackService: TrackService) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || ProductionReleasesConfiguration.detailHeading;
    this.dataService = this.securityViolationsService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
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
