import {Component, Input, OnChanges} from '@angular/core';
import * as d3 from 'd3';

@Component({
  selector: 'app-total-reporting',
  templateUrl: './metric-total-reporting.component.html',
  styleUrls: ['./metric-total-reporting.component.scss']
})
export class TotalReportingComponent implements OnChanges {
  @Input() public totalReporting: number = null;
  @Input() public isComponentList: boolean;

  constructor() { }

  ngOnChanges() {
    setTimeout(() => this.drawVisual(), 0);
  }

  drawVisual() {
    const
      duration = 800,
      value = this.totalReporting * 100,
      width = 200,
      height = 200;

    const dataset = {
        lower: calcPercent(0),
        upper: calcPercent(value)
      },
      radius = Math.min(width, height) / 2.8,
      pie = d3.layout.pie().sort(null);

    const arc = d3.svg.arc()
      .innerRadius(radius * .88)
      .outerRadius(radius);

    d3.select('.chart svg').remove();

    const svg = d3.select('.chart').append('svg')
      .attr('width', width)
      .attr('height', height)
      .append('g')
      .attr('transform', `translate(${width / 2},${height / 2 })`);

    let path = svg.selectAll('path')
      .data(pie(dataset.lower))
      .enter().append('path')
      .attr('class', (d, i) => 'color' + i)
      .attr('d', arc)
      .each(function (d) {
        this._current = d;
      });

    const fillPath = svg.select('.color0');
    const red = '#C21C36';
    const yellow = '#FFBA00';
    const green = '#7ED321';

    const colorScale = d3.scale.linear()
      .domain([0, 0.25, 0.5, 0.75, 1])
      .range([red, red, yellow, green, green]);

    const text = d3.select('.percent-reporting');

    const progress = 0;

    const timeout = setTimeout(function () {
      clearTimeout(timeout);
      path = path.data(pie(dataset.upper));
      path.transition().duration(duration).attrTween('d', function (a) {
        const i = d3.interpolate(this._current, a);
        const i2 = d3.interpolate(progress, value);
        this._current = i(0);
        return function (t) {
          text.text(Math.round(i2(t)));
          fillPath.attr('fill', colorScale(i2(t) / 100));
          return arc(i(t));
        };
      });
    }, 200);

    function calcPercent(percent) {
      return [percent, 100 - percent];
    }
  }
}
