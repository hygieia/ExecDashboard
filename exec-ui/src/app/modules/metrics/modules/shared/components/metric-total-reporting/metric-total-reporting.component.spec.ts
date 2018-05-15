import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalReportingComponent } from './metric-total-reporting.component';

describe('TotalReportingComponent', () => {
  let component: TotalReportingComponent;
  let fixture: ComponentFixture<TotalReportingComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TotalReportingComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TotalReportingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
