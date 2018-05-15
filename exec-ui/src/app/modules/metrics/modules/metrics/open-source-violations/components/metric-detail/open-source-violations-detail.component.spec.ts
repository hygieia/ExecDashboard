import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenSourceViolationsDetailComponent } from './open-source-violations-detail.component';

describe('OpenSourceViolationsDetailComponent', () => {
  let component: OpenSourceViolationsDetailComponent;
  let fixture: ComponentFixture<OpenSourceViolationsDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenSourceViolationsDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenSourceViolationsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
