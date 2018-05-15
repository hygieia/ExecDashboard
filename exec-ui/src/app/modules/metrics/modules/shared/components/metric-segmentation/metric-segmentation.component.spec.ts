import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricSegmentationComponent } from './metric-segmentation.component';

describe('MetricSegmentationComponent', () => {
  let component: MetricSegmentationComponent;
  let fixture: ComponentFixture<MetricSegmentationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MetricSegmentationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetricSegmentationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
