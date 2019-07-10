import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { StaticCodeAnalysisDetailComponent } from './static-code-analysis-detail.component';

describe('StaticCodeAnalysisDetailComponent', () => {
  let component: StaticCodeAnalysisDetailComponent;
  let fixture: ComponentFixture<StaticCodeAnalysisDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ StaticCodeAnalysisDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(StaticCodeAnalysisDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
