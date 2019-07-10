import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/vz/auth.service';
import { Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-header-logo',
  templateUrl: './header-logo.component.html',
  styleUrls: ['./header-logo.component.scss']
})
export class HeaderLogoComponent implements OnInit {
public userName: string;
currentUrl: string;

  constructor(private authService: AuthService, private router: Router) { 

     router.events.subscribe((_: NavigationEnd) => this.currentUrl = _.url);
  }

  ngOnInit() {
    this.authService.currentAuthData.subscribe(data => {this.userName = data['firstname']});
  }

}
