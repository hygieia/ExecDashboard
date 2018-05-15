export class ProductionIncidentsConfiguration {
  public static identifier = 'production-incidents';
  public static previewHeading = 'Production Incidents';
  public static detailHeading = 'Production Incidents';
  public static graphHeading = 'Production Incidents';
  public static buildingBlockLabel = 'Production Incidents';
  public static description = 'Sum of severity 1, 2, 3, 3C and 3D production incidents occurring over the last 90 ' +
    'days. An incident is any event that is not part of the standard operation of a service and that causes or may ' +
    'cause an interruption to, or a reduction in, the quality of that service.';

  public static auxilliaryIdentifier = 'mean-time-to-resolve';
  public static auxilliaryFigureHeading = 'Mean Time to Resolve';
}
