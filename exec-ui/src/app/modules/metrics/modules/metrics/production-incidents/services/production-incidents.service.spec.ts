import {ProductionIncidentsService } from './production-incidents.service';
import { TestBed, inject } from '@angular/core/testing';

import {HttpHandler, HttpClient} from '@angular/common/http';

describe('ProductionIncidentsService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ProductionIncidentsService, HttpClient, HttpHandler]
    });
  });

  it('should be created', inject([ProductionIncidentsService], (service: ProductionIncidentsService) => {
    expect(service).toBeTruthy();
  }));
});
