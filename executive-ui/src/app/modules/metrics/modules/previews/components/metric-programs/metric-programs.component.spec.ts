import {HttpClient, HttpHandler} from '@angular/common/http';
import {OpenSourceViolationsPreviewComponent} from '../../../metrics/open-source-violations/components/metric-preview/open-source-violations-preview.component';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {MetricProgramsComponent} from './metric-programs.component';

describe('MetricProgramsComponent', () => {
  let component: MetricProgramsComponent;
  let fixture: ComponentFixture<MetricProgramsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MetricProgramsComponent, OpenSourceViolationsPreviewComponent],
      providers: [HttpClient, HttpHandler]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetricProgramsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
