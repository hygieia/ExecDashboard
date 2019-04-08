import {HttpClient, HttpHandler} from '@angular/common/http';
import {OpenSourceViolationsPreviewComponent} from '../../../metrics/open-source-violations/components/metric-preview/open-source-violations-preview.component';
import {MetricPreviewsComponent} from '../../../previews/components/metric-previews/metric-previews.component';
import {HeadingComponent} from '../../../../../shared/components/heading/heading.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {DashboardComponent} from './dashboard.component';
import {Router} from '@angular/router';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardComponent, HeadingComponent, MetricPreviewsComponent, OpenSourceViolationsPreviewComponent],
      providers: [HttpClient, HttpHandler, Router]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
