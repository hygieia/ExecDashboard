import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticCodeAnalysisPreviewComponent } from './static-code-analysis-preview.component';

describe('StaticCodeAnalysisPreviewComponent', () => {
  let component: StaticCodeAnalysisPreviewComponent;
  let fixture: ComponentFixture<StaticCodeAnalysisPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaticCodeAnalysisPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaticCodeAnalysisPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
