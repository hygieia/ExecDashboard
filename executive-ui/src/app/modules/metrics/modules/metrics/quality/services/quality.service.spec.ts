import { QualityService } from './quality.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('QualityService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [QualityService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([QualityService], (service: QualityService) => {
    expect(service).toBeTruthy();
  }));
});
