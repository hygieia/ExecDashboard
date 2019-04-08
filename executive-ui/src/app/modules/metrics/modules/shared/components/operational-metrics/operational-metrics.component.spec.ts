import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OperationalMetricsComponent } from './operational-metrics.component';

describe('OperationalMetricsComponent', () => {
  let component: OperationalMetricsComponent;
  let fixture: ComponentFixture<OperationalMetricsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OperationalMetricsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OperationalMetricsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
