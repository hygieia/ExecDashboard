import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SCMCommitsPreviewComponent } from './scm-commits-preview.component';


describe('SCMCommitsPreviewComponent', () => {
  let component: SCMCommitsPreviewComponent;
  let fixture: ComponentFixture<SCMCommitsPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SCMCommitsPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SCMCommitsPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
