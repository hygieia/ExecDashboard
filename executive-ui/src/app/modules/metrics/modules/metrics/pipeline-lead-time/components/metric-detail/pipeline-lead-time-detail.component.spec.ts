import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PipelineLeadTimeDetailComponent } from './pipeline-lead-time-detail.component';

describe('PipelineLeadTimeDetailComponent', () => {
  let component: PipelineLeadTimeDetailComponent;
  let fixture: ComponentFixture<PipelineLeadTimeDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PipelineLeadTimeDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PipelineLeadTimeDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
