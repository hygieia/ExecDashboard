import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionReleasesPreviewComponent } from './production-releases-preview.component';

describe('ProductionReleasesPreviewComponent', () => {
  let component: ProductionReleasesPreviewComponent;
  let fixture: ComponentFixture<ProductionReleasesPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProductionReleasesPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProductionReleasesPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
