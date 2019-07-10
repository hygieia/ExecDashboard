import { HttpClient, HttpHandler } from '@angular/common/http';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenSourceViolationsPreviewComponent } from './open-source-violations-preview.component';

describe('OpenSourceViolationsPreviewComponent', () => {
  let component: OpenSourceViolationsPreviewComponent;
  let fixture: ComponentFixture<OpenSourceViolationsPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenSourceViolationsPreviewComponent ],
      providers: [HttpClient, HttpHandler]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenSourceViolationsPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
