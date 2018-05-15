import { TestBed, inject } from '@angular/core/testing';

import { MetricMapService } from './metric-map.service';

describe('MetricMapService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MetricMapService]
    });
  });

  it('should be created', inject([MetricMapService], (service: MetricMapService) => {
    expect(service).toBeTruthy();
  }));
});
