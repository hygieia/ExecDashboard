import { TestBed, inject } from '@angular/core/testing';

import { PerformanceTestService } from './performance-test.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('PerformanceTestService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PerformanceTestService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([PerformanceTestService], (service: PerformanceTestService) => {
    expect(service).toBeTruthy();
  }));
});
