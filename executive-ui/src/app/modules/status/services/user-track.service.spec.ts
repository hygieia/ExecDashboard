import { TestBed, inject } from '@angular/core/testing';

import { UserTrackService } from './user-track.service';

describe('UserTrackService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserTrackService]
    });
  });

  it('should be created', inject([UserTrackService], (service: UserTrackService) => {
    expect(service).toBeTruthy();
  }));
});
