import { Component, OnInit } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { AuthService } from '../../services/vz/auth.service';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  currentUrl: string;


  constructor(private cookieService: CookieService, private authService: AuthService, private router: Router,
    private route: ActivatedRoute) {
    router.events.subscribe((_: NavigationEnd) => this.currentUrl = _.url);
  }

  ngOnInit() {

    const eid: string = this.cookieService.get('eid');

    const authObject = {
      eid: this.cookieService.get('eid'),
      email: this.cookieService.get('email'),
      firstname: this.cookieService.get('firstname'),
      lastname: this.cookieService.get('lastname'),
      username: this.cookieService.get('username')
    };

    this.authService.setAuth(authObject);
    this.authService.setAuthEid(eid);

    this.authService.getPortfolioId(this.cookieService.get('eid')).subscribe(
      data => {
        this.router.navigate(['portfolio', data]);
      },
      error => {
        console.log(error);
      });

  }

}
