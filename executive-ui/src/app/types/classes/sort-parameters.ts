import { SortParameters as SortParametersInterface } from '../interfaces/sort-parameters';

export class SortParameters implements SortParametersInterface {

    readonly array: any[];
    readonly byProperty: string;
    readonly thenByProperty?: string;

    /**
     * Creates an instance of SortParameters.
    * @param {any[]} array The array to sort.
     * @param {string} byProperty Primary sorting constraint.
     * @param {string} [thenByProperty] [optional]: Disambiguation sorting constraint.
     * @memberof SortParameters
     */
    constructor(array: any[], byProperty: string, thenByProperty?: string) {
      this.array = array;
      this.byProperty = byProperty;
      this.thenByProperty = thenByProperty;
    }

  }
