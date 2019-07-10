import { HttpClient, HttpHandler } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PipelineLeadTimePreviewComponent } from './pipeline-lead-time-preview.component';

describe('PipelineLeadTimePreviewComponent', () => {
  let component: PipelineLeadTimePreviewComponent;
  let fixture: ComponentFixture<PipelineLeadTimePreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PipelineLeadTimePreviewComponent],
      providers: [HttpClient, HttpHandler]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PipelineLeadTimePreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
