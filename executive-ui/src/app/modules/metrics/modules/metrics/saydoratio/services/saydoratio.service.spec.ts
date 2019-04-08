import {SayDoRatioService } from './saydoratio.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('SaydoRatioService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SayDoRatioService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([SayDoRatioService], (service:  SayDoRatioService) => {
    expect(service).toBeTruthy();
  }));
});
