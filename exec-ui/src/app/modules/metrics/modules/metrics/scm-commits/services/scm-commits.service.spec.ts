import { SCMCommitsService } from './scm-commits.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('SCMCommitsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SCMCommitsService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([SCMCommitsService], (service: SCMCommitsService) => {
    expect(service).toBeTruthy();
  }));
});
