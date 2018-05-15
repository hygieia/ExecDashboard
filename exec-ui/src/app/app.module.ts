import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SortingService} from './services/sorting.service';
import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {HttpClientModule} from '@angular/common/http';
import {DirectoryModule} from './modules/directory/directory.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {AppRoutingModule} from './app.routing';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {HeaderLogoComponent} from './components/header-logo/header-logo.component';
import {ElementResizeDetectorService} from './services/element-resize-detector.service';
import {MetricsModule} from './modules/metrics/metrics.module';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HeaderLogoComponent,
  ],
  imports: [
    AppRoutingModule,
    BrowserModule,
    HttpClientModule,
    FlexLayoutModule,
    DirectoryModule,
    MetricsModule,
    BrowserAnimationsModule
  ],
  providers: [SortingService, ElementResizeDetectorService],
  bootstrap: [AppComponent]
})
export class AppModule {
}
