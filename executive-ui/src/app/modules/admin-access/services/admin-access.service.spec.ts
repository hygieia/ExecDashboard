import { TestBed } from '@angular/core/testing';

import { AdminAccessService } from './admin-access.service';

describe('AdminAccessService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdminAccessService = TestBed.get(AdminAccessService);
    expect(service).toBeTruthy();
  });
});
