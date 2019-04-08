import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InstanceComponent } from './instance.component';

describe('InstanceComponent', () => {
  let component: InstanceComponent;
  let fixture: ComponentFixture<InstanceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InstanceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InstanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
