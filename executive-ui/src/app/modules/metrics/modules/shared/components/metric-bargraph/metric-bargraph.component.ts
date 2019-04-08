import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { MetricGraphModel } from '../../component-models/metric-graph-model';
import { PresentationFunctions as display } from '../../utils/presentation-functions';
import * as d3 from 'd3';
import * as moment from 'moment';
import { ElementResizeDetectorService } from '../../../../../../services/element-resize-detector.service';
import { PresentationFunctions } from '../../utils/presentation-functions';

@Component({
  selector: 'app-metric-bargraph',
  templateUrl: 'metric-bargraph.component.html',
  styleUrls: ['metric-bargraph.component.scss']
})
export class MetricBargraphComponent implements OnInit, OnChanges {
  @Input() public model: MetricGraphModel;

  public isStories: boolean = false;
  public cloudLabel: string;

  constructor(private elementResizeDetectorService: ElementResizeDetectorService) { }

  ngOnInit() {

    this.cloudLabel = this.getmonth();

    setTimeout(() => this.buildGraph(), 0);

  }

  ngOnChanges(changes: SimpleChanges) {

  }

  protected getmonth() {
    const d = new Date();
    const monthNames = ['DECEMBER', 'JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE',
      'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER'
    ];
    return monthNames[d.getMonth()] + ' COST';
  }

  public buildGraph() {
    if (!PresentationFunctions.determineBrowser().isFirefox) {
      d3.select('.metric-graph').style('margin-bottom', 0);
    }

    const model = this.model;

    if (model.valueLabel == 'STORIES COMPLETED') {
      this.isStories = true;
    }

    const apiData = this.model.values;

    const width = 675;
    const height = 225;

    d3.select('.metric-graph svg').remove();
    d3.select('.metric-graph .tooltip').remove();

    const svg = d3.select('.metric-graph').append('svg')
      .classed('svg-content-responsive', true)
      .attr('preserveAspectRatio', 'xMinYMin meet')
      .attr('viewBox', '0 0 650 225')
      .attr('fill', 'transparent')
      .attr('height', height)
      .attr('width', width);

    const xScale = d3.scale.ordinal()
      .rangeRoundBands([0, width], 0.5, 0.5);

    const yScale = d3.scale.linear()
      .range([height, 0]);

    const xAxis = d3.svg.axis()
      .scale(xScale)
      .orient('bottom')

    const yAxis = d3.svg.axis()
      .scale(yScale)
      .orient('left')

    var data = [];


    apiData.forEach((value: number, key: number) => {
      data.push({ 'daysAgo': (key), 'issues': value });
    });

    const monthNames = ['JANUARY', 'FEBRUARY', 'MARCH', 'APRIL', 'MAY', 'JUNE',
      'JULY', 'AUGUST', 'SEPTEMBER', 'OCTOBER', 'NOVEMBER', 'DECEMBER'
    ];

    const monthNamesMin = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUNE',
      'JULY', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'
    ];

    data = data.reverse()

    if (this.isStories) {
      let stack = [];

      let a1 = new Date()


      data.forEach(d => {
        a1 = new Date()
        a1.setDate(a1.getDate() - Number(d['daysAgo']))

        d['daysAgo'] = monthNamesMin[a1.getMonth()] + ' ' + a1.getDate()
        stack.push(monthNamesMin[a1.getMonth()] + ' ' + a1.getDate())
      })



      let newStack = [];

      for (var i = 0; i < stack.length - 1; i++) {
        newStack.push(stack[i] + ' - ' + stack[i + 1])
      }

      newStack.push(stack[stack.length - 1] + ' - ' + 'Today')


      for (var i = 0; i < data.length; i++) {
        data[i]['daysAgo'] = newStack[i]
      }


    } else {
      data.forEach(d => {


        let a1 = new Date()
        a1.setDate(a1.getDate() - Number(d['daysAgo']) - 1)
        d['daysAgo'] = monthNames[a1.getMonth()]



      })

      data = data.reverse()
    }


    xScale.domain(data.map(function (d) { return d['daysAgo']; }));
    yScale.domain([0, d3.max(data, function (d) { return d['issues']; })]);



    svg.selectAll('rect')
      .data(data)
      .enter()
      .append('rect')
      .attr('height', 0)
      .attr('y', height)
      .transition().duration(3000)
      .delay(function (d, i) { return i * 200; })
      .attr({
        'x': function (d) { return xScale(d['daysAgo']); },
        'y': function (d) { return yScale(d['issues']); },
        'width': xScale.rangeBand(),
        'height': function (d) { return height - yScale(d['issues']); }
      })
      .style('fill', function (d, i) { return 'rgb(20, ' + ((i * 30) + 100) + ' ,20)' });
    if (this.isStories) {
      svg.selectAll('text')
        .data(data)
        .enter()
        .append('text')



        .text(function (d) {

          return (d['issues'] + ' Stories');

        })
        .attr({
          'x': function (d) { return xScale(d['daysAgo']) + xScale.rangeBand() / 2; },
          'y': function (d) { return yScale(d['issues']) + 12; },
          'font-family': 'sans-serif',
          'font-size': '12px',
          'font-weight': 'bold',
          'fill': 'white',
          'text-anchor': 'middle'
        });
    } else {
      svg.selectAll('text')
        .data(data)
        .enter()
        .append('text')



        .text(function (d) {


          return Number((d['issues'])).toLocaleString('en-US', { style: 'currency', currency: 'USD' })

        })
        .attr({
          'x': function (d) { return xScale(d['daysAgo']) + xScale.rangeBand() / 2; },
          'y': function (d) { return yScale(d['issues']) + 12; },
          'font-family': 'sans-serif',
          'font-size': '12px',
          'font-weight': 'bold',
          'fill': 'white',
          'text-anchor': 'middle'
        });
    }

    svg.append('g')
      .attr('class', 'x axis')
      .attr('transform', 'translate(0,' + height + ')')
      .call(xAxis)



    svg.append('g')
      .attr('class', 'y axis')
      .call(yAxis)








  }



}


