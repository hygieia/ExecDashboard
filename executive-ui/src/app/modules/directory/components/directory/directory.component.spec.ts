import { PortfolioCardsComponent } from '../portfolio-cards/portfolio-cards.component';
import { HeadingComponent } from '../../../shared/components/heading/heading.component';
import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DirectoryComponent } from './directory.component';
import { PortfolioCardComponent } from '../portfolio-card/portfolio-card.component';
import { HttpClient, HttpHandler } from '@angular/common/http';

describe('DirectoryComponent', () => {
  let component: DirectoryComponent;
  let fixture: ComponentFixture<DirectoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        DirectoryComponent,
        HeadingComponent,
        PortfolioCardsComponent,
        PortfolioCardComponent
      ],
      providers: [HttpClient, HttpHandler]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DirectoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
