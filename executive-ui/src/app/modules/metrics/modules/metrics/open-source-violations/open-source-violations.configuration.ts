export class OpenSourceViolationsConfiguration {
  public static identifier = 'open-source-violations';
  public static api = 'VELOCITY';
  public static previewHeading = 'Velocity';
  public static detailHeading = 'Velocity';
  public static graphHeading = 'Days per Story Point';
  public static buildingBlockLabel = 'Velocity as Days per Story Point';
  public static description = 'Velocity is calculated as the total time taken by user stories divided by the total number of story points. Total time is calculated as the time in days from when a user story is moved to "In Progress" until it is marked as "Done".';
  public static dataSource = 'Data Source: OneJira ';
  public static auxilliaryIdentifier = 'Total Time';
  public static auxilliaryFigureHeading = 'Total Time';
}
