import { TestBed, inject } from '@angular/core/testing';

import { VonkinatorService } from './vonkinator.service';

describe('VonkinatorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VonkinatorService]
    });
  });

  it('should be created', inject([VonkinatorService], (service: VonkinatorService) => {
    expect(service).toBeTruthy();
  }));
});
