import { TestBed, inject } from '@angular/core/testing';

import { TestAutomationService } from './test-automation.service';
import {HttpHandler, HttpClient} from '@angular/common/http';

describe('TestAutomationService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TestAutomationService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([TestAutomationService], (service: TestAutomationService) => {
    expect(service).toBeTruthy();
  }));
});
