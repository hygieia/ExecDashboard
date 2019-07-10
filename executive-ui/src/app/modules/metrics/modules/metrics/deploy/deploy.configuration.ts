export class DeployConfiguration {
  public static identifier = 'deploy';
  public static api = 'DEPLOY';
  public static previewHeading = 'Deploys';
  public static detailHeading = 'Deploys';
  public static graphHeading = 'Deploys';
  public static buildingBlockLabel = 'Total Deploys';
  public static description = 'Total Number of Deployments triggered over the last 90 days in all the Prod and Non-Prod environments. ' +
    'This includes Deployments done at various architectural layers of an application like GUI, Middle ware, Database etc.';
  public static dataSource = 'Data Source: OneJenkins ';
  public static auxilliaryIdentifier = 'Avg Execution Time';
  public static auxilliaryFigureHeading = 'Avg Execution Time';
}
