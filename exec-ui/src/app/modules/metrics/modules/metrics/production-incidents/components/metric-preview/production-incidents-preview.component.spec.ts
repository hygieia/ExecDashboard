import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionIncidentsPreviewComponent } from './production-incidents-preview.component';

describe('ProductionIncidentsPreviewComponent', () => {
  let component: ProductionIncidentsPreviewComponent;
  let fixture: ComponentFixture<ProductionIncidentsPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductionIncidentsPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductionIncidentsPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
