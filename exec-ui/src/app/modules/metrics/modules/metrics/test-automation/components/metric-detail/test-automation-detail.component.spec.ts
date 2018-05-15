import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TestAutomationDetailComponent } from './test-automation-detail.component';

describe('TestAutomationDetailComponent', () => {
  let component: TestAutomationDetailComponent;
  let fixture: ComponentFixture<TestAutomationDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TestAutomationDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestAutomationDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
