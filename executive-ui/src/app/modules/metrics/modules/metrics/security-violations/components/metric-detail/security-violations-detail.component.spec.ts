import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SecurityViolationsDetailComponent } from './security-violations-detail.component';

describe('SecurityViolationsDetailComponent', () => {
  let component: SecurityViolationsDetailComponent;
  let fixture: ComponentFixture<SecurityViolationsDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SecurityViolationsDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SecurityViolationsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
