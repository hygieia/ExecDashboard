export class QualityConfiguration {
  public static identifier = 'quality';
  public static api = 'QUALITY';
  public static previewHeading = 'Quality';
  public static detailHeading = 'Quality';
  public static graphHeading = 'Total Defects';
  public static buildingBlockLabel = 'Defects';
  public static description = 'Sum of Defects for all associated components, including ' +
    'Code Issues, Configuration related issues and Broken Code. Quality is determined based on the bugs created in onejira ' +
    'in the production environment as well from the defects raised in AYS & CMIS ';
  public static dataSource = 'Data Source: OneJira, AYS & CMIS';
}
