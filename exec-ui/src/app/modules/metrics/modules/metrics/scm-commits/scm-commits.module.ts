import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';

import {SharedModule} from '../../../../shared/shared.module';
import {MetricMapService} from '../../shared/services/metric-map.service';
import {DashboardSharedModule} from '../../shared/shared.module';
import {SCMCommitsGraphStrategy} from './strategies/scm-commits-graph-strategy';
import {SCMCommitsPreviewStrategy} from './strategies/scm-commits-preview-strategy';
import {SCMCommitsDetailStrategy} from './strategies/scm-commits-detail-strategy';
import {SCMCommitsBuildingBlocksStrategy} from './strategies/scm-commits-building-blocks-strategy';
import {SCMCommitsPrimaryMetricStrategy} from './strategies/scm-commits-primary-metric-strategy';
import {SCMCommitsTrendStrategy} from './strategies/scm-commits-trend-strategy';
import {SCMCommitsDetailComponent} from './components/metric-detail/scm-commits-detail.component';
import {SCMCommitsPreviewComponent} from './components/metric-preview/scm-commits-preview.component';
import {SCMCommitsRoutingModule} from './scm-commits.routing.module';
import {SCMCommitsConfiguration} from './scm-commits.configuration';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    SCMCommitsRoutingModule
  ],
  declarations: [
    SCMCommitsPreviewComponent,
    SCMCommitsDetailComponent
  ],
  exports: [
    SCMCommitsPreviewComponent,
    SCMCommitsDetailComponent
  ],
  providers: [
    MetricMapService,
    SCMCommitsTrendStrategy,
    SCMCommitsPrimaryMetricStrategy,
    SCMCommitsBuildingBlocksStrategy,
    SCMCommitsDetailStrategy,
    SCMCommitsPreviewStrategy,
    SCMCommitsGraphStrategy,
    SCMCommitsConfiguration
  ]
})

export class SCMCommitsModule {}
