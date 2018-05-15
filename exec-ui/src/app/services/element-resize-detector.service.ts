import {Injectable} from '@angular/core';
import * as elementResizeDetectorMaker from 'element-resize-detector';

@Injectable()
export class ElementResizeDetectorService {
  private resizeDetector: any;

  constructor() {
    this.resizeDetector = elementResizeDetectorMaker({ strategy: 'scroll' });
  }

  addResizeEventListener(target: { element: HTMLElement, handler: Function }) {
    this.resizeDetector.listenTo(target.element, target.handler);
  }

  removeResizeEventListener(element: HTMLElement) {
    this.resizeDetector.uninstall(element);
  }
}
