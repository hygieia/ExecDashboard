import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SayDoRatioPreviewComponent } from './saydoratio-preview.component';

describe('SayDoRatioPreviewComponent', () => {
  let component: SayDoRatioPreviewComponent;
  let fixture: ComponentFixture<SayDoRatioPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SayDoRatioPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SayDoRatioPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
