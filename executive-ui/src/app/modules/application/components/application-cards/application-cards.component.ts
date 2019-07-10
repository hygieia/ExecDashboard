import { Application } from '../../../shared/shared.module';
import { Component, HostBinding, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-application-cards',
  templateUrl: './application-cards.component.html',
  styleUrls: ['./application-cards.component.scss'],
})
export class ApplicationCardsComponent implements OnInit {

  @Input() public applications: Application[];
  public pageNumber: number;

  constructor() { }

  ngOnInit() {
    this.pageNumber = 1;
  }
}
