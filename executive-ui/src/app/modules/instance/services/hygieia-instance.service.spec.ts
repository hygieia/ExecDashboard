import { TestBed, inject } from '@angular/core/testing';

import { HygieiaInstanceService } from './hygieia-instance.service';

describe('HygieiaInstanceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HygieiaInstanceService]
    });
  });

  it('should be created', inject([HygieiaInstanceService], (service: HygieiaInstanceService) => {
    expect(service).toBeTruthy();
  }));
});
