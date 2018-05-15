import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortfolioCardsComponent } from './portfolio-cards.component';
import { Router } from '@angular/router';
import { PortfolioCardComponent } from '../portfolio-card/portfolio-card.component';

describe('PortfolioCardsComponent', () => {
  let component: PortfolioCardsComponent;
  let fixture: ComponentFixture<PortfolioCardsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PortfolioCardsComponent, PortfolioCardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortfolioCardsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
