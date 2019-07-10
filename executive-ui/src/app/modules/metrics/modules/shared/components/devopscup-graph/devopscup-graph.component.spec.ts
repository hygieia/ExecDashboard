import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevopscupGraphComponent } from './devopscup-graph.component';

describe('DevopscupGraphComponent', () => {
  let component: DevopscupGraphComponent;
  let fixture: ComponentFixture<DevopscupGraphComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevopscupGraphComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevopscupGraphComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
