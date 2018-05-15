import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {ProductListComponent} from './components/product-list/product-list.component';

const routes: Routes = [
  {
    path: 'portfolio/:portfolio-name/:portfolio-lob/products',
    component: ProductListComponent,
    data: { animation: 'products' }
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
