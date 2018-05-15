import {MetricPreviewBaseComponent} from '../../../../shared/components/metric-preview/metric-preview-base.component';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SCMCommitsService} from '../../services/scm-commits.service';
import {SCMCommitsPreviewStrategy} from '../../strategies/scm-commits-preview-strategy';
import {SCMCommitsConfiguration} from '../../scm-commits.configuration';

/**
 * @export
 * @class SCMCommitsPreviewComponent
 * @extends {MetricPreviewBaseComponent}
 * @implements {OnInit}
 */
@Component({
  selector: 'app-scm-commits-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [SCMCommitsService]
})
export class SCMCommitsPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private scmCommitsService: SCMCommitsService,
              protected router: Router,
              public strategy: SCMCommitsPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.scmCommitsService;
    this.metric = SCMCommitsConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
