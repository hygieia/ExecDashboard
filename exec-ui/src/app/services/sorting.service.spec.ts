import { TestBed, inject } from '@angular/core/testing';

import { Executive } from '../modules/shared/domain-models/executive';
import { SortingService } from './sorting.service';
import { Portfolio } from '../modules/shared/domain-models/portfolio';

describe('SortService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SortingService]
    });
  });

  it('should be created', inject([SortingService], (service: SortingService) => {
    expect(service).toBeTruthy();
  }));

  it('should sort Portfolio[] by Executives lastName and then by firstName', inject([SortingService], (service: SortingService) => {
    const testPortfolioArray = new Array<Portfolio>();
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'joe', lastName: 'andrews', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'andrew', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'AndrEws', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'joh', lastName: 'andrews', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'jOhn', lastName: 'andrews', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'John', lastName: 'andrews', role: 'tester'}
    });

    const expectedPortfolioArray = new Array<Portfolio>();
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'andrew', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'joe', lastName: 'andrews', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'joh', lastName: 'andrews', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'AndrEws', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'jOhn', lastName: 'andrews', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'John', lastName: 'andrews', role: 'tester'}
    });

    const testRun =
      service.sort<Portfolio>({
        array: testPortfolioArray,
        byProperty: 'executive.lastName',
        thenByProperty: 'executive.firstName'
      });

    expect(testRun).toEqual(expectedPortfolioArray);
  }));

  it('should sort Portfolio[] by Executives with duplicate entries', inject([SortingService], (service: SortingService) => {
    const testPortfolioArray = new Array<Portfolio>();
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'andrews', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'candrews', role: 'tester'}
    });
    testPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'andrews', role: 'tester'}
    });

    const expectedPortfolioArray = new Array<Portfolio>();
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'andrews', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'andrews', role: 'tester'}
    });
    expectedPortfolioArray.push({
      id: null,
      lob: null,
      name: null,
      executive: {firstName: 'john', lastName: 'candrews', role: 'tester'}
    });

    const testRun =
    service.sort<Portfolio>({
      array: testPortfolioArray,
      byProperty: 'executive.lastName',
      thenByProperty: 'executive.firstName'
    });
    expect(testRun).toEqual(expectedPortfolioArray);

  }));
});
