import { CodeRepoPreviewStrategy } from '../../strategies/code-repo-preview-strategy';
import { MetricPreviewBaseComponent } from '../../../../shared/components/metric-preview/metric-preview-base.component';
import { Component, OnInit } from '@angular/core';
import { CodeRepoService } from '../../services/code-repo.service';
import { Router } from '@angular/router';
import { CodeRepoConfiguration } from '../../code-repo.configuration';

@Component({
  selector: 'app-code-repo-preview',
  templateUrl: '../../../../shared/components/metric-preview/metric-preview-base.component.html',
  styleUrls: ['../../../../shared/components/metric-preview/metric-preview-base.component.scss'],
  providers: [CodeRepoService]
})
export class CodeRepoPreviewComponent extends MetricPreviewBaseComponent implements OnInit {

  constructor(private CodeRepoService: CodeRepoService,
    protected router: Router,
    public strategy: CodeRepoPreviewStrategy) { super(); }

  ngOnInit() {
    this.dataService = this.CodeRepoService;
    this.metric = CodeRepoConfiguration.identifier;
    this.loadMetricSummaryData();
  }
}
