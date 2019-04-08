import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecurityViolationsPreviewComponent } from './security-violations-preview.component';

describe('SecurityViolationsPreviewComponent', () => {
  let component: SecurityViolationsPreviewComponent;
  let fixture: ComponentFixture<SecurityViolationsPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecurityViolationsPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecurityViolationsPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
