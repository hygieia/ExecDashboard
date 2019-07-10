export class CloudConfiguration {
  public static identifier = 'cloud';
  public static api = 'CLOUD';
  public static previewHeading = 'Cloud Cost';
  public static detailHeading = 'Cloud Metrics';
  public static graphHeading = getmonth();
  public static buildingBlockLabel = getmonth();
  public static description = 'Cost incurred by the AWS Resources for the last month and its Compliance Report.';
  public static dataSource = 'Data Source: Cost - CloudHealth, AWS Resource Details - Cloud Custodian';
  public static auxilliaryIdentifier = 'Prod Instances';
  public static auxilliaryFigureHeading = 'Cloud Cost';

}
function getmonth() {
  const d = new Date();
  const monthNames = ['December', 'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November'
  ];
  return monthNames[d.getMonth()] + ' Cost';
}
