import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ExternalMonitorComponent } from './externalmonitor.component';

describe('ExternalMonitorComponent', () => {
  let component: ExternalMonitorComponent;
  let fixture: ComponentFixture<ExternalMonitorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ExternalMonitorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ExternalMonitorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
