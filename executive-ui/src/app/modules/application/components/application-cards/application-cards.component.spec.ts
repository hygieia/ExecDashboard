import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ApplicationCardsComponent } from './application-cards.component';
import { Router } from '@angular/router';
import { ApplicationCardComponent } from '../application-card/application-card.component';

describe('ApplicationCardsComponent', () => {
  let component: ApplicationCardsComponent;
  let fixture: ComponentFixture<ApplicationCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ApplicationCardsComponent, ApplicationCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
