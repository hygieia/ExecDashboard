import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TraceabilityDetailComponent } from './traceability-detail.component';

describe('TraceabilityDetailComponent', () => {
  let component: TraceabilityDetailComponent;
  let fixture: ComponentFixture<TraceabilityDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TraceabilityDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TraceabilityDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
