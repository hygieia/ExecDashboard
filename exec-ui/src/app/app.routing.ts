import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';

export const routes: Routes = [
  {
    path: 'directory',
    loadChildren: './modules/directory/directory.module#DirectoryModule',
  },
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob',
    loadChildren: './modules/metrics/modules/dashboard/dashboard.module#DashboardModule',
  },
  {
    path: '',
    redirectTo: 'directory',
    pathMatch: 'full',
  }
];

export const AppRoutingModule: ModuleWithProviders = RouterModule.forRoot(routes, {useHash: true, enableTracing: false});
