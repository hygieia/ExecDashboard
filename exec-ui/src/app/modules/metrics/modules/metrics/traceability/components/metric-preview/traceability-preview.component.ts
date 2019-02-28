import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {TraceabilityPreviewStrategy} from '../../strategies/traceability-preview-strategy';
import {TraceabilityConfiguration} from '../../traceability.configuration';
import {TraceabilityService} from '../../services/traceability.service';

/**
 * @export
 * @class TraceabilityPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */

@Component({
    selector: 'app-traceability-card',
    templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
    styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
    providers: [TraceabilityService]
})

export class TraceabilityPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

    constructor(private traceabilityService: TraceabilityService,
                protected router: Router,
                public strategy: TraceabilityPreviewStrategy) { super(); }

    ngOnInit() {
        this.dataService = this.traceabilityService;
        this.metric = TraceabilityConfiguration.identifier;
        this.loadMetricSummaryData();
    }
}
