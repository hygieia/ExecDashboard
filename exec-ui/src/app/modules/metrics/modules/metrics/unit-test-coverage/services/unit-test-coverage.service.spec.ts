import { TestBed, inject } from '@angular/core/testing';

import { UnitTestCoverageService } from './unit-test-coverage.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('UnitTestCoverageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UnitTestCoverageService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([UnitTestCoverageService], (service: UnitTestCoverageService) => {
    expect(service).toBeTruthy();
  }));
});
