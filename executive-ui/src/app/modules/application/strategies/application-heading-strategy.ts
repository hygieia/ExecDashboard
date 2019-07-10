import {Strategy} from '../../shared/strategies/strategy';
import {HeadingModel} from '../../shared/component-models/heading-model';
import { Injectable } from '@angular/core';

@Injectable()
export class ApplicationHeadingStrategy implements Strategy<void, HeadingModel> {
  parse() {
    const model = new HeadingModel();
    model.icon = 'components';
    model.primaryText = 'Select Application';

    return model;
  }
}
