import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MetricAuxiliaryFigureComponent } from './metric-auxiliary-figure.component';

describe('MetricAuxiliaryFigureComponent', () => {
  let component: MetricAuxiliaryFigureComponent;
  let fixture: ComponentFixture<MetricAuxiliaryFigureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MetricAuxiliaryFigureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetricAuxiliaryFigureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
