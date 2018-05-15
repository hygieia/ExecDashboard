import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UnitTestCoverageDetailComponent } from './unit-test-coverage-detail.component';

describe('UnitTestCoverageDetailComponent', () => {
  let component: UnitTestCoverageDetailComponent;
  let fixture: ComponentFixture<UnitTestCoverageDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UnitTestCoverageDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UnitTestCoverageDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
