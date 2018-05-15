import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SCMCommitsDetailComponent } from './scm-commits-detail.component';

describe('SCMCommitsDetailComponent', () => {
  let component: SCMCommitsDetailComponent;
  let fixture: ComponentFixture<SCMCommitsDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SCMCommitsDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SCMCommitsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
