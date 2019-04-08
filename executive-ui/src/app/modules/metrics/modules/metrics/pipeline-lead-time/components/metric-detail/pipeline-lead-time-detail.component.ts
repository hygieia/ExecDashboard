import { MetricDetailModel } from '../../../../shared/component-models/metric-detail-model';
import { PipelineLeadTimeDetailStrategy } from '../../strategies/pipeline-lead-time-detail-strategy';
import { PipelineLeadTimeService } from '../../services/pipeline-lead-time.service';
import { Component, OnInit } from '@angular/core';
import { MetricDetailBaseComponent } from '../../../../shared/components/metric-detail/metric-detail-base.component';
import { ActivatedRoute, Router, Params } from '@angular/router';
import { PortfolioService } from '../../../../../../shared/shared.module';
import { AuthService } from '../../../../../../../services/vz/auth.service';
import { TrackService } from '../../../../../../../services/vz/track.service';
import { PipelineLeadTimeBuildingBlocksStrategy } from '../../strategies/pipeline-lead-time-building-blocks-strategy';
import { ProductService } from '../../../../shared/services/product.service';
import { PipelineLeadTimeConfiguration } from '../../pipeline-lead-time.configuration';
import { Location } from '@angular/common';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';

@Component({
  selector: 'app-pipeline-lead-time-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    PipelineLeadTimeService,
    Ng4LoadingSpinnerComponent,
    ProductService
  ]
})
export class PipelineLeadTimeDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
    private activatedRoute: ActivatedRoute,
    private pipelineLeadTimeService: PipelineLeadTimeService,
    protected portfolioService: PortfolioService,
    protected productService: ProductService,
    public buildingBlockStrategy: PipelineLeadTimeBuildingBlocksStrategy,
    public detailStrategy: PipelineLeadTimeDetailStrategy,
    protected ng4LoadingSpinnerService: Ng4LoadingSpinnerService,
    protected location: Location,
    protected authService: AuthService,
    protected trackService: TrackService) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || PipelineLeadTimeConfiguration.detailHeading;
    this.dataService = this.pipelineLeadTimeService;

    this.activatedRoute.params.subscribe((params: Params) => {
      this.portfolioId = params['portfolio-id'];
      this.productId = params['product-id'];
      this.loadData();
      this.headingModel = this.getHeader();
    });
  }

  getHeader() {
    return {
      primaryText: this.heading || PipelineLeadTimeConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
