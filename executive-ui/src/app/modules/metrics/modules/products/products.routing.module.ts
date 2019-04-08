import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {ProductListComponent} from './components/product-list/product-list.component';
import {PortfolioListComponent} from './components/portfolio-list/portfolio-list.component';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-id/products',
    component: ProductListComponent,
    data: { animation: 'products' }
  },
   {
    path: 'portfolio/:portfolio-id/executives',
    component: PortfolioListComponent,
    data: { animation: 'executives' }
  }
];

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [],
  exports: [RouterModule]
})

export class ProductsRoutingModule { }
