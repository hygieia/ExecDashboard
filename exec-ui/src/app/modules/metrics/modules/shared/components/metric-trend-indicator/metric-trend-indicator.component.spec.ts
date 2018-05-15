import {HttpClient, HttpHandler} from '@angular/common/http';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {MetricTrendIndicatorComponent} from './metric-trend-indicator.component';

describe('MetricTrendIndicatorComponent', () => {
  let component: MetricTrendIndicatorComponent;
  let fixture: ComponentFixture<MetricTrendIndicatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [MetricTrendIndicatorComponent],
      providers: [HttpClient, HttpHandler]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetricTrendIndicatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
