import {Component, OnInit, Input, OnChanges} from '@angular/core';
import {BuildingBlockModel} from '../../component-models/building-block-model';

@Component({
  selector: 'app-building-blocks',
  templateUrl: './building-blocks.component.html',
  styleUrls: ['./building-blocks.component.scss']
})
export class BuildingBlocksComponent implements OnInit, OnChanges {
  @Input() public buildingBlocks: BuildingBlockModel[];
  @Input() public isComponentList: boolean;

  public reportingBuildingBlocks: number;
  public totalBuildingBlocks: number;
  public icon: string;

  constructor() { }

  ngOnInit() {
    this.icon = this.isComponentList ? 'components' : 'box';
  }

  ngOnChanges() {
    if (this.buildingBlocks) {

      this.reportingBuildingBlocks = this.buildingBlocks.filter(this.reportingProductsFilter).length;
      this.totalBuildingBlocks = this.buildingBlocks.length;
    }
  }

  reportingProductsFilter(buildingBlock: BuildingBlockModel) {
    return buildingBlock.metrics.length;
  }
}
