import {ProgramRoutingModule} from './programs.routing.module';
import {SharedModule} from '../../../shared/shared.module';
import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FlexLayoutModule} from '@angular/flex-layout';
import {PreviewsModule} from '../previews/previews.module';
import {ProgramComponent} from './components/programs/programs.component';
import {ProgramDetailsComponent} from './components/program-details/program-details.component';


@NgModule({
  imports: [
    FlexLayoutModule,
    CommonModule,
    SharedModule,
    ProgramRoutingModule,
    PreviewsModule
  ],
  declarations: [
    ProgramComponent, 
    ProgramDetailsComponent
  ],
  exports: [
    ProgramComponent, 
    ProgramDetailsComponent
  ],
  providers: [
    //MetricBuildingBlocksMapStrategy
  ]
})

export class ProgramModule {
}
