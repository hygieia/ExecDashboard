import {Injectable} from '@angular/core';
import {SortParametersFor} from '../types/classes/sort-parameters-for';

/**
 * @export
 * @class SortingService
 */
@Injectable()
export class SortingService {

  constructor() { }

   /**
    * @template T
    * @param {SortParametersFor<T>} sortParameters The parameters object for the sort.
    * @returns {T[]} The sorted array, otherwise the original array.
    * @memberof SortingService
    */
  sort<T>(sortParameters: SortParametersFor<T>): T[] {
    return sortParameters.byProperty.length ? sortParameters.array.slice().sort(by) : sortParameters.array;

    function by(a, b): number {
      let c = a, d = b;
      const byProp = sortParameters.byProperty.split('.');
      for (let i = 0; i < byProp.length; i++) {
        c = c[byProp[i]];
        d = d[byProp[i]];
      }

      if (c.toString().toLowerCase() > d.toString().toLowerCase()) {
        return 1;
      }
      if (c.toString().toLowerCase() < d.toString().toLowerCase()) {
        return -1;
      }
      if (sortParameters.thenByProperty && sortParameters.thenByProperty.length) {
        return thenBy(a, b);
      }
      return 0;
    }

    function thenBy(a, b): number {
      let c = a, d = b;
      const thenByProp = sortParameters.thenByProperty.split('.');
      for (let i = 0; i < thenByProp.length; i++) {
        c = c[thenByProp[i]];
        d = d[thenByProp[i]];
      }

      if (c.toString().toLowerCase() > d.toString().toLowerCase()) {
        return 1;
      }
      if (c.toString().toLowerCase() < d.toString().toLowerCase()) {
        return -1;
      }
      return 0;
    }
  }
}

