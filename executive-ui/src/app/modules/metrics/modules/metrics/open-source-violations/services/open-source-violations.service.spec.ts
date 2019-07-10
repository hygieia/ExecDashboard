import { TestBed, inject } from '@angular/core/testing';

import { OpenSourceViolationsService } from './open-source-violations.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('OpenSourceScanIssuesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [OpenSourceViolationsService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([OpenSourceViolationsService], (service: OpenSourceViolationsService) => {
    expect(service).toBeTruthy();
  }));
});
