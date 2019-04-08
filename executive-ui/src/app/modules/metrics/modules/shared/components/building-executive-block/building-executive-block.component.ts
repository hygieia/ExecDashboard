import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { BuildingBlockModel } from '../../component-models/building-block-model';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-building-executive-block',
  templateUrl: './building-executive-block.component.html',
  styleUrls: ['./building-executive-block.component.scss']
})

export class BuildingExecutiveBlockComponent implements OnInit {
  @Input() public buildingBlock: BuildingBlockModel;
  @Input() public cardsList: string[];
  @Output() public showSpinnerForApp = new EventEmitter<string>();

  public flexSize: string;

  constructor(private router: Router,
    private route: ActivatedRoute) {
    this.showSpinnerForApp.emit('hidden');
  }

  ngOnInit() {
    this.showSpinnerForApp.emit('hidden');
  }

  showMetrics(metric: string) {
    if (this.cardsList.includes(metric) || (this.cardsList.includes('pipeline-lead-time') && metric == undefined)) {
      return false;
    }
    return true;
  }

  hasMetricReports() {
    return this.buildingBlock.metrics.some((metric) => {
      return !metric.isEmpty;
    });
  }

  isNavigationValid(): boolean {
    return this.hasMetricReports() && (!!this.buildingBlock.detail.url);
  }

  gotToBuildingBlockDetails() {
    this.router.navigate(['portfolio', this.buildingBlock.lob]);
  }

}
