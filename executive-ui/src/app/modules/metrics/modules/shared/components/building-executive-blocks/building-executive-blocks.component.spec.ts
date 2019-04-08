import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BuildingExecutiveBlocksComponent } from './building-executive-blocks.component';

describe('BuildingExecutiveBlocksComponent', () => {
  let component: BuildingExecutiveBlocksComponent;
  let fixture: ComponentFixture<BuildingExecutiveBlocksComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BuildingExecutiveBlocksComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BuildingExecutiveBlocksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
