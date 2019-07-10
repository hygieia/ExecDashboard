import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SortingService} from './services/sorting.service';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {ApplicationModule} from './modules/application/application.module';
import {DirectoryModule} from './modules/directory/directory.module';
import {ProgramModule} from './modules/metrics/modules/programs/programs.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {AppRoutingModule} from './app.routing';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {HeaderLogoComponent} from './components/header-logo/header-logo.component';
import {ElementResizeDetectorService} from './services/element-resize-detector.service';
import {MetricsModule} from './modules/metrics/metrics.module';
import {CookieService} from 'ngx-cookie-service';
import {AuthService} from './services/vz/auth.service';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown/angular2-multiselect-dropdown';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { StatusModule } from './modules/status/status.module';
import { ExternalMonitorModule } from './modules/externalMonitor/externalmonitor.module';
import { CodedirectoryComponent } from './modules/codedirectory/codedirectory.component';
import { SidebarComponent } from './modules/sidebar/sidebar.component';
import { Angular2FontawesomeModule } from 'angular2-fontawesome/angular2-fontawesome';
import { TrackService } from './services/vz/track.service';




@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HeaderLogoComponent,
    CodedirectoryComponent,
    SidebarComponent  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    FlexLayoutModule,
    ApplicationModule,
    DirectoryModule,
    ProgramModule,
    MetricsModule,
    BrowserAnimationsModule,
    AngularMultiSelectModule,
    StatusModule,
    ExternalMonitorModule,
    FormsModule, ReactiveFormsModule,
    InfiniteScrollModule,
    Angular2FontawesomeModule
  ],
  providers: [SortingService, ElementResizeDetectorService, CookieService, AuthService, TrackService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
