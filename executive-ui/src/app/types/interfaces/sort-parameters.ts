/**
 * @export
 * @interface SortParameters
 */
export interface SortParameters {
  /**
   * @type {any[]}
   * @memberof SortParameters
   */
  readonly array: any[];

  /**
   * @type {string}
   * @memberof SortParameters
   */
  readonly byProperty: string;

  /**
   * @type {string}
   * @memberof SortParameters
   */
  readonly thenByProperty?: string;

  [propName: string]: any;
}
