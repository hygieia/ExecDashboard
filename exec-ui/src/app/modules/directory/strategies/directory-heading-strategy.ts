import {Strategy} from '../../shared/strategies/strategy';
import {HeadingModel} from '../../shared/component-models/heading-model';
import { Injectable } from '@angular/core';

@Injectable()
export class DirectoryHeadingStrategy implements Strategy<void, HeadingModel> {
  parse() {
    const model = new HeadingModel();
    model.icon = 'person';
    model.primaryText = 'Select a Portfolio';

    return model;
  }
}
