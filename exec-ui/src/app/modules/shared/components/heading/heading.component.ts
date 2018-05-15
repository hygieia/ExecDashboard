import { Component, Input, OnInit } from '@angular/core';
import {HeadingModel} from '../../component-models/heading-model';
import {Router} from '@angular/router';
import {NavigationModel} from '../../component-models/navigation-model';

@Component({
  selector: 'app-heading',
  templateUrl: './heading.component.html',
  styleUrls: ['./heading.component.scss']
})
export class HeadingComponent implements OnInit {
  @Input() public model: HeadingModel;

  constructor(private router: Router) { }

  ngOnInit() {
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
