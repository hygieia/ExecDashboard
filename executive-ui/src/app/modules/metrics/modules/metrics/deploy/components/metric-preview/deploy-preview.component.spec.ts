import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DeployPreviewComponent } from './deploy-preview.component';

describe('DeployPreviewComponent', () => {
  let component: DeployPreviewComponent;
  let fixture: ComponentFixture<DeployPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DeployPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DeployPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
