import {HttpClient, HttpHandler} from '@angular/common/http';
import {MetricPreviewsComponent} from '../../../previews/components/metric-previews/metric-previews.component';
import {HeadingComponent} from '../../../../../shared/components/heading/heading.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ProductListComponent} from './product-list.component';
import {Router} from '@angular/router';

describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ProductListComponent, HeadingComponent, MetricPreviewsComponent],
      providers: [HttpClient, HttpHandler, Router]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
