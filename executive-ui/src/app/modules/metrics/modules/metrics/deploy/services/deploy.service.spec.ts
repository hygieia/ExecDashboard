import {DeployService } from './deploy.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('DeployService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DeployService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([DeployService], (service: DeployService) => {
    expect(service).toBeTruthy();
  }));
});
