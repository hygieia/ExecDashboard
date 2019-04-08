import { SecurityViolationsService } from './security-violations.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('SecurityViolationsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SecurityViolationsService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([SecurityViolationsService], (service: SecurityViolationsService) => {
    expect(service).toBeTruthy();
  }));
});
