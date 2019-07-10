import { StaticCodeAnalysisService } from './static-code-analysis.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('StaticCodeAnalysisService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StaticCodeAnalysisService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([StaticCodeAnalysisService], (service: StaticCodeAnalysisService) => {
    expect(service).toBeTruthy();
  }));
});
