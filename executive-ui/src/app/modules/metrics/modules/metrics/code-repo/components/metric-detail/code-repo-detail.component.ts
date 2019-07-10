import { MetricDetailModel } from '../../../../shared/component-models/metric-detail-model';
import { CodeRepoDetailStrategy } from '../../strategies/code-repo-detail-strategy';
import { CodeRepoService } from '../../services/code-repo.service';
import { Component, OnInit } from '@angular/core';
import { MetricDetailBaseComponent } from '../../../../shared/components/metric-detail/metric-detail-base.component';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { PortfolioService } from '../../../../../../shared/shared.module';
import { AuthService } from '../../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../../services/vz/track.service';
import { CodeRepoBuildingBlocksStrategy } from '../../strategies/code-repo-building-blocks-strategy';
import { ProductService } from '../../../../shared/services/product.service';
import { CodeRepoConfiguration } from '../../code-repo.configuration';
import { Location } from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-code-repo-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    CodeRepoService,
    Ng4LoadingSpinnerComponent,
    ProductService
  ]
})
export class CodeRepoDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
    private activatedRoute: ActivatedRoute,
    private CodeRepoService: CodeRepoService,
    protected portfolioService: PortfolioService,
    protected productService: ProductService,
    public buildingBlockStrategy: CodeRepoBuildingBlocksStrategy,
    public detailStrategy: CodeRepoDetailStrategy,
    protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    protected location: Location,
    protected authService: AuthService,
    protected trackService: TrackService) {

    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.dataService = this.CodeRepoService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || CodeRepoConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}