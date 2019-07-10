import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'totalReportingPercent'
})
export class TotalReportingPercentPipe implements PipeTransform {

  transform(value: number, args?: any): number {
    return Math.round(value * 100);
  }

}
