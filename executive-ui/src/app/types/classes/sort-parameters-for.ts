import { SortParametersFor as SortParametersForInterface } from '../interfaces/sort-parameters-for';

export class SortParametersFor<T> implements SortParametersForInterface<T> {

    readonly array: T[];
    readonly byProperty: string;
    readonly thenByProperty?: string;

    /**
     * Creates an instance of SortParameters.
     * @param {T[]} array The array to sort.
     * @param {string} byProperty Primary sorting constraint.
     * @param {string} [thenByProperty] [optional]: Disambiguation sorting constraint.
     * @memberof SortParameters
     */
    constructor(array: T[], byProperty: string, thenByProperty?: string) {
      this.array = array;
      this.byProperty = byProperty;
      this.thenByProperty = thenByProperty;
    }

  }
