import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {PipelineLeadTimeDetailStrategy} from '../../strategies/pipeline-lead-time-detail-strategy';
import {PipelineLeadTimeService} from '../../services/pipeline-lead-time.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {PipelineLeadTimeBuildingBlocksStrategy} from '../../strategies/pipeline-lead-time-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {PipelineLeadTimeConfiguration} from '../../pipeline-lead-time.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-pipeline-lead-time-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    PipelineLeadTimeService,
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
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || PipelineLeadTimeConfiguration.detailHeading;
    this.dataService = this.pipelineLeadTimeService;

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
      primaryText: this.heading || PipelineLeadTimeConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
