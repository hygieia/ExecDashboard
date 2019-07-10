import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CloudDetailComponent } from './cloud-detail.component';

describe('CloudDetailComponent', () => {
  let component: CloudDetailComponent;
  let fixture: ComponentFixture<CloudDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CloudDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CloudDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
