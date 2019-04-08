import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevopscupMetricsComponent } from './devopscup-metrics.component';

describe('DevopscupMetricsComponent', () => {
  let component: DevopscupMetricsComponent;
  let fixture: ComponentFixture<DevopscupMetricsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevopscupMetricsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevopscupMetricsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
