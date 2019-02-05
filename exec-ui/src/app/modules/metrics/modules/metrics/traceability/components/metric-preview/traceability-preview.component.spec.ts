import { HttpClient, HttpHandler } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TraceabilityPreviewComponent } from './traceability-preview.component';

describe('TraceabilityPreviewComponent', () => {
  let component: TraceabilityPreviewComponent;
  let fixture: ComponentFixture<TraceabilityPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TraceabilityPreviewComponent],
      providers: [HttpClient, HttpHandler]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TraceabilityPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
