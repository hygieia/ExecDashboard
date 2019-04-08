import { TestBed, inject } from '@angular/core/testing';

import { DevopscupService } from './devopscup.service';

describe('DevopscupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DevopscupService]
    });
  });

  it('should be created', inject([DevopscupService], (service: DevopscupService) => {
    expect(service).toBeTruthy();
  }));
});
