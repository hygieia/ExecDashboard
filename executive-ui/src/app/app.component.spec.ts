import { HeaderComponent } from './components/header/header.component';
import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { HeaderLogoComponent } from './components/header-logo/header-logo.component';
import { AppRoutingModule } from './app.routing';
describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        HeaderComponent,
        HeaderLogoComponent,
      ],
      imports: [
        AppRoutingModule
      ]
    }).compileComponents();
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
