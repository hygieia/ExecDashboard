import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CodedirectoryComponent } from './codedirectory.component';

describe('CodedirectoryComponent', () => {
  let component: CodedirectoryComponent;
  let fixture: ComponentFixture<CodedirectoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CodedirectoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CodedirectoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
