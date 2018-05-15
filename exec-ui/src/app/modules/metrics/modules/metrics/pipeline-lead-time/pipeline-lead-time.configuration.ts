export class PipelineLeadTimeConfiguration {
  public static identifier = 'pipeline-lead-time';
  public static previewHeading = 'Average Pipeline Lead Time';
  public static detailHeading = 'Average Pipeline Lead Time';
  public static graphHeading = 'Average Lead Time';
  public static buildingBlockLabel = 'Average Pipeline Lead Time';
  public static description = 'Average amount of time from code commit to production deployment for all ' +
    'associated components. Cycle time is a key indicator of overall DevOps efficiency and is collected ' +
    'by Hygieia from continuous integration and delivery tools.';
}
