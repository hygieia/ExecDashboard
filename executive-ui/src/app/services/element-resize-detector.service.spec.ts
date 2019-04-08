import { TestBed, inject } from '@angular/core/testing';

import { ElementResizeDetectorService } from './element-resize-detector.service';

describe('ElementResizeDetectorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ElementResizeDetectorService]
    });
  });

  it('should be created', inject([ElementResizeDetectorService], (service: ElementResizeDetectorService) => {
    expect(service).toBeTruthy();
  }));
});
