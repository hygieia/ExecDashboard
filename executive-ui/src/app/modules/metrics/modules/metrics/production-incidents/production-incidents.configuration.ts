export class ProductionIncidentsConfiguration {
  public static identifier = 'production-incidents';
  public static api = 'PRODUCTION_INCIDENTS';
  public static previewHeading = 'Events';
  public static detailHeading = 'Events';
  public static graphHeading = 'Production Events';
  public static buildingBlockLabel = 'Production Events';
  public static description = 'Sum of severity 1 and 2 production outages occurring over the last 90 ' +
    'days. An outage is any event that is not part of the standard operation of a service and that causes or may ' +
    'cause an interruption to, or a reduction in, the quality of that service.';
  public static dataSource = 'Data Source: ServiceNow (OPSaaS)';
  public static auxilliaryIdentifier = 'mean-time-to-resolve';
  public static auxilliaryFigureHeading = 'Mean Time to Resolve';
}
