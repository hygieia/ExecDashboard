import { TestBed, inject } from '@angular/core/testing';

import { PipelineLeadTimeService } from './pipeline-lead-time.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('PipelineLeadTimeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PipelineLeadTimeService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([PipelineLeadTimeService], (service: PipelineLeadTimeService) => {
    expect(service).toBeTruthy();
  }));
});
