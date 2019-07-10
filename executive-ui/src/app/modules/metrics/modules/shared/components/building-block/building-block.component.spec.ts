import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildingBlockComponent } from './building-block.component';

describe('BuildingBlockComponent', () => {
  let component: BuildingBlockComponent;
  let fixture: ComponentFixture<BuildingBlockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingBlockComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingBlockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
