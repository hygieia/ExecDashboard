import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {StaticCodeAnalysisDetailStrategy} from '../../strategies/static-code-analysis-detail-strategy';
import {StaticCodeAnalysisService} from '../../services/static-code-analysis.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {StaticCodeAnalysisBuildingBlocksStrategy} from '../../strategies/static-code-analysis-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {StaticCodeAnalysisConfiguration} from '../../static-code-analysis.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-static-code-analysis-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    StaticCodeAnalysisService,
    ProductService
  ]
})
export class StaticCodeAnalysisDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private securityViolationsService: StaticCodeAnalysisService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: StaticCodeAnalysisBuildingBlocksStrategy,
              public detailStrategy: StaticCodeAnalysisDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || StaticCodeAnalysisConfiguration.detailHeading;
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
      primaryText: this.heading || StaticCodeAnalysisConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
