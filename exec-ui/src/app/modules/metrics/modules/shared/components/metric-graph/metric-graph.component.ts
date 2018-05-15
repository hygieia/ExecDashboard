import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {MetricGraphModel} from '../../component-models/metric-graph-model';
import {PresentationFunctions as display} from '../../utils/presentation-functions';
import * as d3 from 'd3';
import * as moment from 'moment';
import {ElementResizeDetectorService} from '../../../../../../services/element-resize-detector.service';
import { PresentationFunctions } from '../../utils/presentation-functions';

@Component({
  selector: 'app-metric-graph',
  templateUrl: 'metric-graph.component.html',
  styleUrls: ['metric-graph.component.scss']
})
export class MetricGraphComponent implements OnInit, OnChanges {
  @Input() public model: MetricGraphModel;

  constructor(private elementResizeDetectorService: ElementResizeDetectorService) { }

  ngOnInit() { }

  ngOnChanges(changes: SimpleChanges) {
    if (this.model) {
      setTimeout(() => this.buildGraph(), 0);
    }
  }

  protected buildGraph() {
    if (!PresentationFunctions.determineBrowser().isFirefox) {
      d3.select('.metric-graph').style('margin-bottom', 0);
    }

    const model = this.model;

    const apiData = this.model.values;

    const graphData = [
      ...apiData.slice(0, 89).map((x, i) => ({daysAgo: i, issues: x}))
    ];

    const width = 750,
          height = 225;

    let tooltipTopAdjustment = 190,
        graphHeadingHeight = 23;

    d3.select('.metric-graph svg').remove();
    d3.select('.metric-graph .tooltip').remove();

    const graph = d3.select('#metric-graph')
      .on('scroll', function () {
        div.style('opacity', 0);
      });

    const graphHeading = document.getElementsByClassName('active-count')[0] as HTMLElement;

    this.elementResizeDetectorService.addResizeEventListener({
      element: graphHeading,
      handler: display.debounce({
        func: (element: HTMLElement) => {
          if (graphHeadingHeight !== element.clientHeight) {
            tooltipTopAdjustment += element.clientHeight - graphHeadingHeight;
            graphHeadingHeight = element.clientHeight;
          }
        },
        wait: 200
      })
    });

    const graphWindow = d3.select(window)
      .on('scroll', function () {
        div.style('opacity', 0);
    });

    const vis = d3.select('.metric-graph').append('svg')
      .classed('svg-content-responsive', true)
      .attr('preserveAspectRatio', 'xMinYMin meet')
      .attr('viewBox', '0 0 755 225')
      .attr('fill', 'transparent')
      .attr('height', '221.39px')
      .attr('width', '738px');

    const dayScale = d3.scale.linear()
      .domain([0, 44, 89])
      .range([width - 18, (0.5) * (width - 22), 40]);

    const countInfo = countScaleDomain(graphData.map(x => x.issues));

    const countScale = d3.scale.linear()
      .domain(countInfo.domain)
      .range([height - 27, 5]);

    const yAxis = d3.svg.axis()
      .scale(countScale)
      .orient('left')
      .tickSize(-width)
      .tickPadding(6)
      .tickFormat(this.model.isRatio ? x => x + '%' : standardFormat)
      .tickValues(countInfo.ticks);

    vis.append('g')
      .attr('class', 'axis')
      .attr('transform', 'translate(30,0)')
      .call(yAxis);

    const lines = vis.append('g')
      .attr('class', 'lines');

    const markers = vis.append('g')
      .attr('class', 'points');

    const div = d3.select('.metric-graph').append('div')
      .attr('class', 'tooltip')
      .style('opacity', 0);

    const line = d3.svg.line()
      .x((d, i) => dayScale(i))
      .y(d => countScale(d.issues));

    lines.append('path')
      .datum(graphData)
      .attr('fill', 'none')
      .attr('stroke-linejoin', 'round')
      .attr('stroke-linecap', 'round')
      .attr('stroke-width', 1.5)
      .attr('d', line);

    markers.selectAll('.linear.point')
      .data(graphData)
      .enter().append('circle')
      .attr({
        'class': 'linear point',
        r: 2.5,
        transform: function (d, i) {
          return `translate(${dayScale(i)},${countScale(d.issues)})`;
        }
      })
      .on('mouseover', function (d, i) {
        div.html(toolTip(d))
          .style('top', `${toolTipPositionTop(this) - containerPositionTop() - window.scrollY + tooltipTopAdjustment}px`);
        div.transition()
          .duration(10)
          .style('opacity', .9);
      })
      .on('mouseout', function () {
        div.transition()
          .duration(50)
          .style('opacity', 0);
      });

    markers.selectAll('.linear.label')
      .data(Array.from(Array(90).keys()))
      .enter().append('text')
      .attr({
        'class': 'linear label',
        transform: function (d) {
          return `translate(${dayScale(d)},${(height - 1)})`;
        }
      })
      .text(function (d) {
        switch (d) {
          case 0:
            return 'TODAY';
          case 14:
            return '15';
          case 29:
            return '30';
          case 44:
            return '45';
          case 59:
            return '60';
          case 74:
            return '75';
          case 89:
            return '90';
        }
      });

    function toolTip(d) {
      return `<strong>${issue(d.issues)}</strong> on ${getToolTipDate(d)}`;
    }

    function getToolTipDate(d) {
      return  moment().subtract(d.daysAgo, 'days').format('MMM D');
    }

    function issue(issueCount) {
      return model.toolTipLabel(issueCount);
    }

    function toolTipPositionLeft(element) {
      const rect = element.getBoundingClientRect();
      return rect.left + (rect.right - rect.left) / 2;
    }

    function toolTipPositionTop(element) {
      const rect = element.getBoundingClientRect();
      return rect.top + (rect.bottom - rect.top) / 2;
    }

    function containerPositionLeft() {
      const container = document.getElementById('metric-graph');
      const rect = container.getBoundingClientRect();
      return rect.left;
    }

    function containerPositionTop() {
      const container = document.getElementById('metric-graph');
      const rect = container.getBoundingClientRect();
      return rect.top;
    }

    function standardFormat(x) {
      if (x >= 1000) {
        return (x / 1000) + 'K';
      }
      return x;
    }

    function countScaleDomain(data) {
      const minData = minOfArray(data);
      const maxData = maxOfArray(data);
      const intervalSpanAmt = intervalSpan(minData, maxData);

      // Special case to avoid having only one axis line
      if (intervalSpanAmt === 0) {
        return {
          domain: [data[0] - 1, data[0] + 1],
          ticks: [data[0] - 1, data[0], data[0] + 1]
        };
      }

      // Special case to avoid having only two axis lines
      if (maxData - minData === 1) {
        return {
          domain: [minData, maxData + 1],
          ticks: [minData, minData + 1, minData + 2]
        };
      }

      const ticks = [Math.floor(minData / intervalSpanAmt) * intervalSpanAmt];
      let cursor = ticks[0];
      while (cursor < maxData) {
        cursor += intervalSpanAmt;
        ticks.push(cursor);
      }

      return {
        domain: [ticks[0], ticks[ticks.length - 1]],
        ticks
      };

      function intervalSpan(min, max) {
        const minMaxDiff = max - min;

        if (minMaxDiff <= 0) {
          return 0;
        }
        if (minMaxDiff <= 10) {
          return 1;
        }
        if (minMaxDiff <= 20) {
          return 2;
        }
        if (minMaxDiff <= 100) {
          return 10;
        }
        if (minMaxDiff <= 200) {
          return 20;
        }
        if (minMaxDiff <= 500) {
          return 50;
        }

        return (Math.ceil(minMaxDiff / 1000) * 1000) / 10;
      }

      function minOfArray(array) {
        if (array.length === 0) {
          return 0;
        }
        if (array.length === 1) {
          return array[0];
        }
        return array.reduce((s, x) => s < x ? s : x);
      }

      function maxOfArray(array) {
        return array.reduce((s, x) => s > x ? s : x, 0);
      }
    }
  }
}
