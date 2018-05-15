export class SecurityViolationsConfiguration {
  public static identifier = 'security-violations';
  public static previewHeading = 'Security Violations';
  public static detailHeading = 'Security Violations';
  public static graphHeading = 'Total Violations';
  public static buildingBlockLabel = 'Security Violations';
  public static description = 'Sum of known, unresolved security violations for all associated components, including ' +
    'violations with blocker, critical and major severity. Security violations are collected by Hygieia from various ' +
    'software security scanning tools.';
}
