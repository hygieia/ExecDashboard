import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VonkinatorComponent } from './vonkinator.component';

describe('VonkinatorComponent', () => {
  let component: VonkinatorComponent;
  let fixture: ComponentFixture<VonkinatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VonkinatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VonkinatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
