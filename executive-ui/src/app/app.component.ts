import { Component } from '@angular/core';
import {trigger, state, style, animate, transition, query} from '@angular/animations';
import { routes} from './app.routing';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  animations: [
    trigger('navigationAnimation', [
      transition('* <=> *', [
        query(':enter',
          style({
            opacity: 0.5,
            position: 'fixed',
            width: '100%',
          }),
          {optional: true}),
        query(':leave',
          animate('0s ease-in-out',
            style({
              opacity: 0.5,
              position: 'fixed',
              width: '100%',
            })
          ),
          {optional: true}),
        query(':enter',
          animate('.5s ease-in',
            style({
              opacity: 1,
            })
          ),
          {optional: true})
      ])
    ])
  ]
})

export class AppComponent {
  getRouteAnimation(outlet) {
    return outlet.activatedRouteData.animation;
  }
}
