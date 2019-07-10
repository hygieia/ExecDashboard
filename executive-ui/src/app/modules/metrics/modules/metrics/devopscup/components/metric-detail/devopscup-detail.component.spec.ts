import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevopscupDetailComponent } from './devopscup-detail.component';

describe('DevopscupDetailComponent', () => {
  let component: DevopscupDetailComponent;
  let fixture: ComponentFixture<DevopscupDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevopscupDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevopscupDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
