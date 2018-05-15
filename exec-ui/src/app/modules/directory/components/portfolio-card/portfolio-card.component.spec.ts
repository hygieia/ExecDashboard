import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PortfolioCardComponent } from './portfolio-card.component';
import { Router } from '@angular/router';

describe('PortfolioCardComponent', () => {
  let component: PortfolioCardComponent;
  let fixture: ComponentFixture<PortfolioCardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PortfolioCardComponent ],
      providers: [Router]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortfolioCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
