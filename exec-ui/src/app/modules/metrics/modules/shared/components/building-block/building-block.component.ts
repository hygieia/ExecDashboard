import {Component, OnInit, Input} from '@angular/core';
import {BuildingBlockModel} from '../../component-models/building-block-model';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-building-block-card',
  templateUrl: './building-block.component.html',
  styleUrls: ['./building-block.component.scss']
})
export class BuildingBlockComponent implements OnInit {
  @Input() public buildingBlock: BuildingBlockModel;
  @Input() public isComponent: boolean;

  constructor(private router: Router,
              private route: ActivatedRoute) {
  }

  ngOnInit() {
  }

  gotToBuildingBlockDetails() {
    localStorage.clear()
    if (!!this.isComponent) {
      window.open(this.buildingBlock.detail.url, '_blank');
    } else {
      if (!this.buildingBlock.detail) {
        this.router.navigate(['product', this.buildingBlock.id], {relativeTo: this.route});
      } else if (this.buildingBlock.detail.url) {
        this.router.navigateByUrl(this.buildingBlock.detail.url);
      } else {
        this.router.navigate(this.buildingBlock.detail.commands, {relativeTo: this.route});
      }
    }
  }

  hasMetricReports() {
    return this.buildingBlock.metrics.some((metric) => {
      return !metric.isEmpty;
    });
  }

  isNavigationValid(): boolean {
    return this.hasMetricReports() && (!this.isComponent || !!this.isComponent && !!this.buildingBlock.detail.url);
  }
}
