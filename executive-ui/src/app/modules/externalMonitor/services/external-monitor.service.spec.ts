import { TestBed, inject } from '@angular/core/testing';

import { ExternalMonitorService } from './external-monitor.service';

describe('ExternalMonitorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ExternalMonitorService]
    });
  });

  it('should be created', inject([ExternalMonitorService], (service: ExternalMonitorService) => {
    expect(service).toBeTruthy();
  }));
});
