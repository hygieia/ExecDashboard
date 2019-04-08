import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevopscupPreviewComponent } from './devopscup-preview.component';

describe('DevopscupPreviewComponent', () => {
  let component: DevopscupPreviewComponent;
  let fixture: ComponentFixture<DevopscupPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevopscupPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevopscupPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
