import {HttpClient, HttpHandler} from '@angular/common/http';
import {OpenSourceViolationsPreviewComponent} from '../../../metrics/open-source-violations/components/metric-preview/open-source-violations-preview.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MetricPreviewsComponent} from './metric-previews.component';

describe('MetricPreviewsComponent', () => {
  let component: MetricPreviewsComponent;
  let fixture: ComponentFixture<MetricPreviewsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MetricPreviewsComponent, OpenSourceViolationsPreviewComponent],
      providers: [HttpClient, HttpHandler]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetricPreviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
