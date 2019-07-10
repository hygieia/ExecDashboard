import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { MetricGraphModel } from '../../component-models/metric-graph-model';
import { PresentationFunctions as display } from '../../utils/presentation-functions';
import * as d3 from 'd3';
import * as moment from 'moment';
import { ElementResizeDetectorService } from '../../../../../../services/element-resize-detector.service';
import { PresentationFunctions } from '../../utils/presentation-functions';

@Component({
  selector: 'app-metric-graph',
  templateUrl: 'metric-graph.component.html',
  styleUrls: ['metric-graph.component.scss']
})
export class MetricGraphComponent implements OnInit, OnChanges {
  @Input() public model: MetricGraphModel;
  public xydata: Object = {};
  public isJira: boolean = false;
  public isTest: boolean = false;

  public projectLabel: boolean = false;
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
    //  console.log('ApiData: ', this.model.values);



    const graphData = [
      //   ...apiData.slice(0, 30).map((x, i) => ({daysAgo: i, issues: x})),
      //   ...apiData.slice(30, 31).map(x => ({daysAgo: 30, issues: x})),
      //   ...apiData.slice(60, 61).map(x => ({daysAgo: 60, issues: x})),
      //   ...apiData.slice(90, 91).map(x => ({daysAgo: 90, issues: x}))
    ];


    let checkIfMultiline = false;
    apiData.forEach((value: number, key: number) => {
      if (key > 91) {
        checkIfMultiline = true;
        this.projectLabel = true;

      }
    });

    const graphData1 = [];
    if (checkIfMultiline) {
      apiData.forEach((value: number, key: number) => {
        if (key < 90) {
          graphData.push({ 'daysAgo': key, 'issues': value });
        } else {
          graphData1.push({ 'daysAgo': (key - 90), 'issues': value });
        }

      });

    } else {
      apiData.forEach((value: number, key: number) => {
        graphData.push({ 'daysAgo': key, 'issues': value });
      });
    }

    graphData.sort(function (a, b) { return a.daysAgo - b.daysAgo });
    graphData1.sort(function (a, b) { return a.daysAgo - b.daysAgo });

    const referenceData = [];
    for (let graphDatas of graphData) {
      referenceData.push(graphDatas.daysAgo);
    }

    const width = 675;
    const height = 225;

    let tooltipLeftAdjustment = -5;
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

    const labelDataSet = [];
    if (graphData.length > 0) {
      labelDataSet.push(0);
      labelDataSet.push(Math.round(graphData.length / 2));
      labelDataSet.push(graphData.length - 1);
    }

    const vis = d3.select('.metric-graph').append('svg')
      .classed('svg-content-responsive', true)
      .attr('preserveAspectRatio', 'xMinYMin meet')
      .attr('viewBox', '0 0 650 225')
      .attr('fill', 'transparent')
      .attr('height', '221.39px')
      .attr('width', '738px');

    const dayScale = d3.scale.linear()
      .domain([0, Math.round(graphData.length * 0.83)])
      .range([width - 22, (7 / 35) * (width - 22), 40]);

    const dayScale2 = d3.scale.linear()
      .domain([0, Math.round(graphData1.length * 0.83)])
      .range([width - 22, (7 / 35) * (width - 22), 40]);

    const countInfo = countScaleDomain(graphData.map(x => x.issues));


    const countScale = d3.scale.linear()
      .domain(countInfo.domain)
      .range([height - 27, 5]);

    const countScale2 = d3.scale.linear()
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

    const markers2 = vis.append('g')
      .attr('class', 'points2');

    const div = d3.select('.metric-graph').append('div')
      .attr('class', 'tooltip')
      .style('opacity', 0);

    const line = d3.svg.line()
      .x((d, i) => dayScale(i))
      .y(d => countScale(d.issues));

    const line1 = d3.svg.line()
      .x((d, i) => dayScale(i))
      .y(d => countScale(d.issues));

    lines.append('path')
      .datum(graphData)
      .attr('fill', 'none')
      .attr('stroke', '#FFBA00')
      .attr('stroke-linejoin', 'round')
      .attr('stroke-linecap', 'round')
      .attr('stroke-width', 1.5)
      .attr('d', line);

    lines.append('path')
      .datum(graphData1)
      .attr('fill', 'none')
      .attr('stroke', '#7ED321')
      .attr('stroke-linejoin', 'round')
      .attr('stroke-linecap', 'round')
      .attr('stroke-width', 1.5)
      .attr('d', line1);


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
        tooltipLeftAdjustment = -5;
        div.html(toolTip(d))
          .style('top', `${toolTipPositionTop(this) - containerPositionTop() - window.scrollY + tooltipTopAdjustment}px`)
          .style('left', `${Math.round(toolTipPositionLeft(this) - containerPositionLeft() - window.scrollX + 175)}px`);

        tooltipLeftAdjustment = toolTipPositionLeft(this) - div.node().getBoundingClientRect().x;

        div
          .style('left', `${toolTipPositionLeft(this) - containerPositionLeft() - window.scrollX - tooltipLeftAdjustment + 175}px`);

        div.transition()
          .duration(10)
          .style('opacity', .9);
      })
      .on('mouseout', function () {
        div.transition()
          .duration(50)
          .style('opacity', 0);
      });

    //-------

    markers2.selectAll('.linear.point')
      .data(graphData1)
      .enter().append('circle')
      .attr({
        'class': 'linear point',
        r: 2.5,
        transform: function (d, i) {
          return `translate(${dayScale2(i)},${countScale2(d.issues)})`;
        }
      })
      .on('mouseover', function (d, i) {
        tooltipLeftAdjustment = -5;
        div.html(toolTip2(d))
          .style('top', `${toolTipPositionTop(this) - containerPositionTop() - window.scrollY + tooltipTopAdjustment}px`)
          .style('left', `${Math.round(toolTipPositionLeft(this) - containerPositionLeft() - window.scrollX + 175)}px`);

        tooltipLeftAdjustment = toolTipPositionLeft(this) - div.node().getBoundingClientRect().x;

        div
          .style('left', `${toolTipPositionLeft(this) - containerPositionLeft() - window.scrollX - tooltipLeftAdjustment + 175}px`);

        div.transition()
          .duration(10)
          .style('opacity', .9);
      })
      .on('mouseout', function () {
        div.transition()
          .duration(50)
          .style('opacity', 0);
      });


    //--------

    if (this.model.valueLabel == 'DAYS PER STORY POINT' || this.model.valueLabel == 'WORK IN PROGRESS' || this.model.valueLabel == 'CYCLE TIME'
      || this.model.valueLabel == 'STORIES') {
      markers.selectAll('.linear.label')
        .data(labelDataSet)
        .enter().append('text')
        .attr({
          'class': 'linear label',
          transform: function (d) {
            return `translate(${dayScale(d)},${(height - 1)})`;
          }
        })
        .text(function (d) {
          var datasets = graphData.find(x => x.daysAgo === referenceData[d]);
          if (datasets != undefined) {
            var days = datasets.daysAgo;
            switch (days) {
              case 0:
                return 'TODAY';
              default:
                const m = new Date();
                m.setDate(m.getDate() - Number(days));
                const date: String = m.toDateString().substring(4);
                const n = new Date();
                n.setDate(n.getDate() - Number(days - 30));
                const datum: String = n.toDateString().substring(4);
                return (date).substring(0, date.length - 4) + '-' + (datum).substring(0, date.length - 4);
            }
          }
        });
    }
    else {
      markers.selectAll('.linear.label')
        .data(labelDataSet)
        .enter().append('text')
        .attr({
          'class': 'linear label',
          transform: function (d) {
            return `translate(${dayScale(d)},${(height - 1)})`;
          }
        })
        .text(function (d) {
          var datasets = graphData.find(x => x.daysAgo === referenceData[d]);
          if (datasets != undefined) {
            var days = datasets.daysAgo;
            switch (days) {
              case 0:
                return 'TODAY';
              default:
                let m = new Date();
                m.setDate(m.getDate() - Number(days));
                var date: String = m.toDateString().substring(4);
                return (date).substring(0, date.length - 4);
            }
          }
        });
    }

    function toolTip(d) {
      return `<strong>${issue(d.issues)}</strong> on ${getToolTipDate(d)}`;
    }

    function toolTip2(d) {
      return `<strong>${issue(d.issues)}</strong>  Successful on ${getToolTipDate(d)}`;
    }

    function getToolTipDate(d) {
      return moment().subtract(d.daysAgo, 'days').format('MMM D');
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
