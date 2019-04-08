import { Application } from '../../../shared/shared.module';
import { Component, ElementRef, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-application-card',
  templateUrl: './application-card.component.html',
  styleUrls: ['./application-card.component.scss']
})
export class ApplicationCardComponent implements OnInit {

  @Input() public application: Application;
  public id: string;
  public name: string;
  public role: string;
  public lob: string;
  public initials: string;

  constructor(private router: Router) {
  }

  ngOnInit() {

    this.id = this.application.appId;
    this.name = this.application.appName;
    this.role = this.application.appAcronym;
    this.lob = this.application.lob;
    this.initials = this.application.appId;
  }

  goToApplication(id: string) {
    this.router.navigate(['application', id]);
  }
}
