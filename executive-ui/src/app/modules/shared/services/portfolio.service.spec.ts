import { TestBed, inject } from '@angular/core/testing';

import { PortfolioService } from './portfolio.service';
import { HttpClient, HttpHandler } from '@angular/common/http';
import { HeadingComponent } from '../components/heading/heading.component';

describe('PortfolioService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PortfolioService, HttpClient, HttpHandler],
      declarations: [HeadingComponent]
    });
  });

  it('should be created', inject([PortfolioService], (service: PortfolioService) => {
    expect(service).toBeTruthy();
  }));
});
