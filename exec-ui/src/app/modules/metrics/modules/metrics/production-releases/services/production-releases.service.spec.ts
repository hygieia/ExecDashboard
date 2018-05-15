import {ProductionReleasesService } from './production-releases.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('ProductionReleasesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProductionReleasesService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([ProductionReleasesService], (service: ProductionReleasesService) => {
    expect(service).toBeTruthy();
  }));
});
