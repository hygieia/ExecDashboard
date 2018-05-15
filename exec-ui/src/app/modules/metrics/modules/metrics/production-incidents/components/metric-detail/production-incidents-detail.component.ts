import {MetricDetailModel} from '../../../../shared/component-models/metric-detail-model';
import {ProductionIncidentsDetailStrategy} from '../../strategies/production-incidents-detail-strategy';
import {ProductionIncidentsService} from '../../services/production-incidents.service';
import {Component, OnInit} from '@angular/core';
import {MetricDetailBaseComponent} from '../../../../shared/components/metric-detail/metric-detail-base.component';
import {ActivatedRoute, Router, Params} from '@angular/router';
import {PortfolioService} from '../../../../../../shared/shared.module';
import {ProductionIncidentsBuildingBlocksStrategy} from '../../strategies/production-incidents-building-blocks-strategy';
import {ProductService} from '../../../../shared/services/product.service';
import {ProductionIncidentsConfiguration} from '../../production-incidents.configuration';
import {Location} from '@angular/common';

@Component({
  selector: 'app-production-incidents-detail',
  templateUrl: '../../../../shared/components/metric-detail/metric-detail-base.component.html',
  styleUrls: ['../../../../shared/components/metric-detail/metric-detail-base.component.scss'],
  providers: [
    ProductionIncidentsService,
    ProductService
  ]
})
export class ProductionIncidentsDetailComponent extends MetricDetailBaseComponent implements OnInit {

  constructor(protected router: Router,
              private activatedRoute: ActivatedRoute,
              private securityViolationsService: ProductionIncidentsService,
              protected portfolioService: PortfolioService,
              protected productService: ProductService,
              public buildingBlockStrategy: ProductionIncidentsBuildingBlocksStrategy,
              public detailStrategy: ProductionIncidentsDetailStrategy,
              protected location: Location) {
    super();
  }

  ngOnInit() {
    this.metricDetailView = new MetricDetailModel;
    this.heading = this.heading || ProductionIncidentsConfiguration.detailHeading;
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
      primaryText: this.heading || ProductionIncidentsConfiguration.detailHeading,
      crumbs: this.getCrumbs(),
      button: this.getReturnRouteModel(),
      icon: this.getIcon()
    };
  }
}
