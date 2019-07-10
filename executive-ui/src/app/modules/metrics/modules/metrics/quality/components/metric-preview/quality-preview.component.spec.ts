import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QualityPreviewComponent } from './quality-preview.component';

describe('QualityPreviewComponent', () => {
  let component: QualityPreviewComponent;
  let fixture: ComponentFixture<QualityPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QualityPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QualityPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
