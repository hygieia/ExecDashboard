import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { SharedModule } from '../../../../shared/shared.module';
import { MetricMapService } from '../../shared/services/metric-map.service';
import { DashboardSharedModule } from '../../shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Ng4LoadingSpinnerModule, Ng4LoadingSpinnerComponent, Ng4LoadingSpinnerService } from 'ng4-loading-spinner';
import { DevopscupRoutingModule } from './devopscup.routing.module';

import { DevopscupPreviewComponent } from './components/metric-preview/devopscup-preview.component';
import { DevopscupTrendStrategy } from './strategies/devopscup-trend-strategy';
import { DevopscupPrimaryMetricStrategy } from './strategies/devopscup-primary-metric-strategy';
import { DevopscupBuildingBlocksStrategy } from './strategies/devopscup-building-blocks-strategy';
import { DevopscupDetailStrategy } from './strategies/devopscup-detail-strategy';
import { DevopscupPreviewStrategy } from './strategies/devopscup-preview-strategy';
import { DevopscupSegmentationStrategy } from './strategies/devopscup-segmentation-strategy';
import { DevopscupGraphStrategy } from './strategies/devopscup-graph-strategy';
import { DevopsCupConfiguration } from './devopscup.configuration';
import { DevopscupDetailComponent } from './components/metric-detail/devopscup-detail.component';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/multiselect.component';

@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    DashboardSharedModule,
    DevopscupRoutingModule,
    Ng4LoadingSpinnerModule,
    FormsModule, ReactiveFormsModule,
    AngularMultiSelectModule
  ],
  declarations:
    [DevopscupPreviewComponent,
      DevopscupDetailComponent
    ],

  exports: [
    DevopscupPreviewComponent
  ],
  providers: [
    MetricMapService,
    DevopscupTrendStrategy,
    DevopscupPrimaryMetricStrategy,
    DevopscupBuildingBlocksStrategy,
    DevopscupDetailStrategy,
    DevopscupPreviewStrategy,
    DevopscupSegmentationStrategy,
    DevopscupGraphStrategy,
    DevopscupDetailComponent
  ]
})
export class DevopscupModule { }
