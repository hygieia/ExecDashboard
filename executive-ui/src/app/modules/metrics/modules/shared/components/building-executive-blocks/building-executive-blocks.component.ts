import { Component, OnInit, Input, OnChanges } from '@angular/core';
import { BuildingBlockModel } from '../../component-models/building-block-model';
import { ProductService } from '../../services/product.service';
import { saveAs } from 'file-saver/FileSaver';

@Component({
  selector: 'app-building-executive-blocks',
  templateUrl: './building-executive-blocks.component.html',
  styleUrls: ['./building-executive-blocks.component.scss']
})
export class BuildingExecutiveBlocksComponent implements OnInit {
  @Input() public buildingBlocks: BuildingBlockModel[];
  @Input() public cardsList: string[];

  public productService: ProductService;
  public allbuildingBlocks: BuildingBlockModel[];
  private metricToBuildingBlocksMap = [];

  public sortType: String = 'Velocity';
  public sortReverse: Boolean = true;
  public dataAvailable: Boolean = false;
  public pageNumber: number;

  public spinnerVisibility: string = 'visible spinner center';
  dropdownSettingsExecutive = {};
  dropdownListExecutive = [];
  selectedItemsExecutive = [];

  constructor(productService: ProductService) {
    this.productService = productService;
  }

  ngOnInit() {
  }

  setCards(value: string) {
    if (this.cardsList != undefined && this.cardsList.includes(value)) {
      return true;
    }
  }


  downloadExcel() {
    const header = 'Executive, Designation, Application Measured, Updated Time, Velocity, Cycle Time, Security Vulnerabilities, Production Events, Quality, Work In Progress, Stories, Builds, Deploys, Cloud Cost, Code Commit, Say Do, Test Metrics, DevOps Cup\n ';
    let content = '';
    if (this.buildingBlocks !== undefined) {
      for (let i = 0; i < this.buildingBlocks.length; i++) {
        content = content + this.buildingBlocks[i].name + ',';
        content = content + this.buildingBlocks[i].poc + ',';
        content = content + this.buildingBlocks[i].total + ',';
        content = content + this.buildingBlocks[i].lastScanned + ',';
        for (let j = 0; j < this.buildingBlocks[i].metrics.length; j++) {
          content = content + this.buildingBlocks[i].metrics[j].value.value;
          if (this.buildingBlocks[i].metrics[j].value.unit !== undefined) {
            content = content + ' ' + this.buildingBlocks[i].metrics[j].value.unit;
          }
          content = content + ',';
        }
        content = content + '\n';
      }
    }

    const blob = new Blob([header + content], { type: 'text/csv;charset=utf-8' });
    saveAs(blob, 'ExecutiveMetricsReport.csv');

  }

  getCloudName() {
    const d = new Date();
    const monthNames = ['December', 'January', 'February', 'March', 'April', 'May', 'June',
      'July', 'August', 'September', 'October', 'November'
    ];
    return monthNames[d.getMonth()] + ' Cost';
  }

  sort(value: String) {
    if (value !== undefined) {

      let temp: BuildingBlockModel[];
      this.metricToBuildingBlocksMap = [];
      this.sortType = value;
      if (value === 'cloudCost') {
        value = this.getCloudName();
        this.sortType = 'cloudCost';
      }
      this.allbuildingBlocks = this.buildingBlocks;
      if (this.buildingBlocks !== undefined) {
        for (let i = 0; i < this.allbuildingBlocks.length; i++) {
          for (let j = 0; j < this.allbuildingBlocks[i].metrics.length; j++) {
            if (this.allbuildingBlocks[i].metrics[j].value.name === value) {
              this.metricToBuildingBlocksMap.push({ model: this.allbuildingBlocks[i], value: this.allbuildingBlocks[i].metrics[j].value.value });
            }
          }
        }
      }

      if (this.metricToBuildingBlocksMap.length > 0) {
        temp = new Array();
        if (this.sortReverse) {
          this.metricToBuildingBlocksMap.sort(function (a, b) {
            return a.value - b.value;
          });

          this.metricToBuildingBlocksMap.forEach(function (element) {
            temp.push(element.model);
          });
          this.buildingBlocks = temp;

        } else {
          temp = new Array();
          this.metricToBuildingBlocksMap.sort(function (a, b) {
            return b.value - a.value;
          });

          this.metricToBuildingBlocksMap.forEach(function (element) {
            temp.push(element.model);
          });

          this.buildingBlocks = temp;
        }
      }
      this.sortReverse = !this.sortReverse;
    }
  }
  setSpinnerStatusForPortfolio(val: string) {
    this.spinnerVisibility = val + ' spinner center';
  }
}
