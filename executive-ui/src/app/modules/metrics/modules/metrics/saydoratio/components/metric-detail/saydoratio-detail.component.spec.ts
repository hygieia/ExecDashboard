import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SayDoRatioDetailComponent } from './saydoratio-detail.component';

describe('SayDoRatioDetailComponent', () => {
  let component: SayDoRatioDetailComponent;
  let fixture: ComponentFixture<SayDoRatioDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SayDoRatioDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SayDoRatioDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
