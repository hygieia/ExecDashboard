import { TestBed, inject } from '@angular/core/testing';

import { TraceabilityService } from './traceability.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('TraceabilityService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TraceabilityService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([TraceabilityService], (service: TraceabilityService) => {
    expect(service).toBeTruthy();
  }));
});
