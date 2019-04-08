import {HttpClient, HttpHandler} from '@angular/common/http';
import {MetricPreviewsComponent} from '../../../previews/components/metric-previews/metric-previews.component';
import {HeadingComponent} from '../../../../../shared/components/heading/heading.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {PortfolioListComponent} from './portfolio-list.component';
import {Router} from '@angular/router';

describe('PortfolioListComponent', () => {
  let component: PortfolioListComponent;
  let fixture: ComponentFixture<PortfolioListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [PortfolioListComponent, HeadingComponent, MetricPreviewsComponent],
      providers: [HttpClient, HttpHandler, Router]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortfolioListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
