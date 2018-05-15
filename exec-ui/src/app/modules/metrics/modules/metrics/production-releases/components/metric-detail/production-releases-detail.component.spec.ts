import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionReleasesDetailComponent } from './production-releases-detail.component';

describe('ProductionReleasesDetailComponent', () => {
  let component: ProductionReleasesDetailComponent;
  let fixture: ComponentFixture<ProductionReleasesDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductionReleasesDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductionReleasesDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
