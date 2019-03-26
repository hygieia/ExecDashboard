import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PerformanceTestDetailComponent } from './performance-test-detail.component';

describe('PerformanceTestDetailComponent', () => {
  let component: PerformanceTestDetailComponent;
  let fixture: ComponentFixture<PerformanceTestDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PerformanceTestDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PerformanceTestDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
