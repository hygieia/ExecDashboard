import { Routes, RouterModule } from '@angular/router';
import { ModuleWithProviders } from '@angular/core';
import { CodedirectoryComponent } from './modules/codedirectory/codedirectory.component';


export const routes: Routes = [
  {
    path: 'portfolio',
    loadChildren: './modules/directory/directory.module#DirectoryModule',
  },
  {
    path: 'application',
    loadChildren: './modules/application/application.module#ApplicationModule',
  },
  {
    path: 'application/:application-id',
    loadChildren: './modules/metrics/modules/product/product.module#ProductModule'
  },
  {
    path: 'portfolio/:portfolio-id',
    loadChildren: './modules/metrics/modules/dashboard/dashboard.module#DashboardModule',
  },
  {
    path: 'portfolio/status/status',
    loadChildren: './modules/status/status.module#StatusModule',
  },
  {
    path: 'externalmonitor/status',
    loadChildren: './modules/externalMonitor/externalmonitor.module#ExternalMonitorModule',
  },
  {
    path: 'externalmonitor/status/getPatchVersions/:bunit',
    loadChildren: './modules/externalMonitor/externalmonitor.module#ExternalMonitorModule',
  },
  {
    path: '',
    redirectTo: 'directory',
    pathMatch: 'full',
  },
  {
    path: 'programs',
    component: CodedirectoryComponent
  },
  {
    path: 'programs/:portfolio-id',
    loadChildren: './modules/metrics/modules/programs/programs.module#ProgramModule'
  },
];

export const AppRoutingModule: ModuleWithProviders = RouterModule.forRoot(routes, {useHash: true, enableTracing: false});
