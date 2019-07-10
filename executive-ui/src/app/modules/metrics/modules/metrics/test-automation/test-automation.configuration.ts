export class TestAutomationConfiguration {
  public static identifier = 'test-automation';
  public static previewHeading = 'Automated Tests Passing';
  public static detailHeading = 'Automated Tests Passing';
  public static graphHeading = 'Passing';
  public static buildingBlockLabel = 'Automated Tests Passing';
  public static description = 'Percentage of automated acceptance tests that passed during the most recent test ' +
    'execution for each associated component collecting test automation results in Hygieia. Formula: Count of passed ' +
    'automated tests / Count of [Passed + Failed + Skipped] Automated tests.';
}
