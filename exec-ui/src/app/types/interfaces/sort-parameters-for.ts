import { SortParameters } from './sort-parameters';

/**
 * @export
 * @interface SortParametersFor
 * @extends {SortParameters}
 * @template T The type of array to sort.
 */
export interface SortParametersFor<T> extends SortParameters {
  /**
   * @type {T[]}
   * @memberof SortParametersFor
   */
  readonly array: T[];
}
