import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Routes, RouterModule} from '@angular/router';
import {ProductComponent} from './components/product/product.component';

const routes: Routes = [
  {
    path: 'application/:application-id',
    component: ProductComponent,
    data: { animation: 'product' }
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

export class ProductRoutingModule { }
