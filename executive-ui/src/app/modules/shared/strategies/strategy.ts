export interface Strategy<T0, T1> {
  parse(model: T0): T1;
}
