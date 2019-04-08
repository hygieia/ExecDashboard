import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'buildingBlockMetricName'
})
export class BuildingBlockMetricNamePipe implements PipeTransform {

  transform(value: string, args?: any): string {
    if (value) {
      const result = [];
      value.split('-')
        .forEach(v => {
          result.push(v[0].toUpperCase() + v.substring(1));
        });
      return result.join(' ');
    }
  }

}
