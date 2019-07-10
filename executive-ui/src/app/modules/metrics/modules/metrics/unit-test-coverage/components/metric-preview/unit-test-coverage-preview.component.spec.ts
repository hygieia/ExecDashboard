import { HttpClient, HttpHandler } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UnitTestCoveragePreviewComponent } from './unit-test-coverage-preview.component';

describe('UnitTestCoveragePreviewComponent', () => {
  let component: UnitTestCoveragePreviewComponent;
  let fixture: ComponentFixture<UnitTestCoveragePreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UnitTestCoveragePreviewComponent],
      providers: [HttpClient, HttpHandler]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UnitTestCoveragePreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
