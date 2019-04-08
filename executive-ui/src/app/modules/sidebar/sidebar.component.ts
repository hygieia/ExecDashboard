import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, NavigationStart } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})


export class SidebarComponent implements OnInit {

  currentUrl: string;

  constructor(private router: Router, private location: Location) {
    router.events.subscribe((_: NavigationEnd) => this.currentUrl = _.url);
  }

  ngOnInit() {
    //console.log("current URL " +  this.currentUrl )
  }

  // bodyContentClass() {
  //   // router  is an instance of Router, injected in the constructor
  //   var viewLocation = location.pathname;
  //   return viewLocation == '/' || viewLocation == '/portfolio' ? "activated" : '';

  //   //this.router.isActive
  // }

}