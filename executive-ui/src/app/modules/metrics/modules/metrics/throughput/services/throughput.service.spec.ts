import { TestBed, inject } from '@angular/core/testing';

import { ThroughputService } from './throughput.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('OpenSourceScanIssuesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ThroughputService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([ThroughputService], (service: ThroughputService) => {
    expect(service).toBeTruthy();
  }));
});
