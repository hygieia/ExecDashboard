export class PipelineLeadTimeConfiguration {
  public static identifier = 'pipeline-lead-time';
  public static api = 'PIPELINE_LEAD_TIME';
  public static previewHeading = 'Cycle Time';
  public static detailHeading = 'Cycle Time';
  public static graphHeading = 'Cycle Time';
  public static buildingBlockLabel = 'Cycle Time';
  public static description = 'Cycle Time is calculated as average time taken for a change to progress from commit stage to the production stage.' +
    'To enable Cycle Time: Set the product dashboard in Application\'s Hygieia Dashboard. Follow the ';
  public static dataSource = 'Data Source: OneJenkins';
  public static auxilliaryIdentifier = 'Deployment Cadence';
  public static auxilliaryFigureHeading = 'Deployment Cadence';
}
