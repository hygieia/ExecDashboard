import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'totalReporting'
})
export class TotalReportingPipe implements PipeTransform {

  transform(value: number, args?: any): number {
    return 414.690 * (1 - value);
  }
}
