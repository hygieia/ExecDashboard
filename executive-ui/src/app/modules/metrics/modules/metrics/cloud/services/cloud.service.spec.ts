import {CloudService } from './cloud.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('CloudService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CloudService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([CloudService], (service: CloudService) => {
    expect(service).toBeTruthy();
  }));
});
