import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CloudPreviewComponent } from './cloud-preview.component';

describe('CloudPreviewComponent', () => {
  let component: CloudPreviewComponent;
  let fixture: ComponentFixture<CloudPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CloudPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CloudPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
