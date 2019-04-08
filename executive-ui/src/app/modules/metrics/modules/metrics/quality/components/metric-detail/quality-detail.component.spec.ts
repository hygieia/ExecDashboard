import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { QualityDetailComponent } from './quality-detail.component';

describe('QualityDetailComponent', () => {
  let component: QualityDetailComponent;
  let fixture: ComponentFixture<QualityDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ QualityDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(QualityDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
