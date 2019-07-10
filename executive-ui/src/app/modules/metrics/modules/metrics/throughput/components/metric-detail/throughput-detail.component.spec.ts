import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ThroughputDetailComponent } from './throughput-detail.component';

describe('ThroughputDetailComponent', () => {
  let component: ThroughputDetailComponent;
  let fixture: ComponentFixture<ThroughputDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ThroughputDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ThroughputDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
