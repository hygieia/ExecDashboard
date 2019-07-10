import * as moment from 'moment';

export module PresentationFunctions {
  export function calculateLastScannedPresentation(offset: Date) {
    return presentTemporalDifference(calculateTemporalDifference(offset));
  }

  function calculateTemporalDifference(offset: Date) {
    const hours = moment().diff(moment(offset), 'hours');
    const days = hours / 24;
    return { hours: hours, days: days };
  }

  function presentTemporalDifference(timeData: { hours: number, days: number }) {
    if (timeData.days >= 1) {
      return `${Math.round(timeData.days).toLocaleString()} ${Math.round(timeData.days) < 2 ? 'day' : 'days'} old`;
    }
    if (timeData.hours >= 1) {
      return `${timeData.hours} ${timeData.hours === 1 ? 'hour' : 'hours'} old`;
    }
    return '~ 1 hour old';
  }

  export function decimalToPercent(decimal) {
    return Math.floor(decimal * 100);
  }

  export function debounce(options: { func: Function, wait: number, immediate?: any }) {
    let timeout;
    return function() {
      const context = this, args = arguments;
      const later = function() {
        timeout = null;
        if (!options.immediate) {
          options.func.apply(context, args);
        }
      };
      const callNow = options.immediate && !timeout;
      clearTimeout(timeout);
      timeout = setTimeout(later, options.wait);
      if (callNow) {
        options.func.apply(context, args);
      }
    };
  }

  export function determineBrowser() {
    // Opera 8.0+
    const isOpera = (!!window['opr'] && !!window['opr'].addons) || !!window['opera'] || navigator.userAgent.indexOf(' OPR/') >= 0;
    // Firefox 1.0+
    const isFirefox = typeof window['InstallTrigger'] !== 'undefined';
    // Safari 3.0+ "[object HTMLElementConstructor]"
    const isSafari = /constructor/i.test(window['HTMLElement'])
      || (function (p) { return p.toString() === '[object SafariRemoteNotification]'; })
      (!window['safari'] || (typeof window['safari'] !== 'undefined' && window['safari'].pushNotification));
    // Internet Explorer 6-11
    const isIE = /*@cc_on!@*/false || !!document['documentMode'];
    // Edge 20+
    const isEdge = !isIE && !!window['StyleMedia'];
    // Chrome 1+
    const isChrome = !!window['chrome'] && !!window['chrome'].webstore;
    // Blink engine detection
    const isBlink = (isChrome || isOpera) && !!window['CSS'];

    return {
      isOpera: isOpera,
      isFirefox: isFirefox,
      isSafari: isSafari,
      isIE: isIE,
      isEdge: isEdge,
      isChrome: isChrome,
      isBlink: isBlink
    };
  }
}
