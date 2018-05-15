import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionIncidentsDetailComponent } from './production-incidents-detail.component';

describe('ProductionIncidentsDetailComponent', () => {
  let component: ProductionIncidentsDetailComponent;
  let fixture: ComponentFixture<ProductionIncidentsDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductionIncidentsDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductionIncidentsDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
