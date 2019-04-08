import { Component, Input, OnInit, OnChanges } from '@angular/core';
import { HeadingModel } from '../../component-models/heading-model';
import { Router } from '@angular/router';
import { NavigationModel } from '../../component-models/navigation-model';

@Component({
  selector: 'app-heading',
  templateUrl: './heading.component.html',
  styleUrls: ['./heading.component.scss']
})
export class HeadingComponent implements OnInit {
  @Input() public model: HeadingModel;
  @Input() public noOfApps: number;
  constructor(private router: Router) { }

  ngOnInit() {

  }

  ngOnChanges() {
    if (this.model && this.noOfApps) {
      if (this.model.primaryText != undefined && this.model.primaryText.search('Portfolio') != -1) {
        this.model.primaryText = this.model.primaryText.substring(0, this.model.primaryText.search('Portfolio')) + 'LOB'
      }
      if (this.model.button != undefined && this.model.button.label.toLocaleLowerCase() == 'change portfolio') {
        this.model.button.label = 'CHANGE EXECUTIVE'
      }
    }
  }

  navigate() {
    if (this.model.button.action) {
      this.model.button.action();
    } else {
      this.router.navigate(this.model.button.commands, this.model.button.extras);
    }
  }

  followCrumb(crumb: NavigationModel) {
    this.router.navigate(crumb.commands, crumb.extras);
  }
}
