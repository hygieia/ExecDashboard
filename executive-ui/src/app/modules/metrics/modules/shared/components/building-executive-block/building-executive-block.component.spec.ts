import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildingExecutiveBlockComponent } from './building-executive-block.component';

describe('BuildingExecutiveBlockComponent', () => {
  let component: BuildingExecutiveBlockComponent;
  let fixture: ComponentFixture<BuildingExecutiveBlockComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingExecutiveBlockComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingExecutiveBlockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
