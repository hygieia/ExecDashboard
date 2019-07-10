export class SecurityViolationsConfiguration {
  public static identifier = 'security-violations';
  public static api = 'SECURITY_VIOLATIONS';
  public static previewHeading = 'Security';
  public static detailHeading = 'Security';
  public static graphHeading = 'Total Vulnerabilities';
  public static buildingBlockLabel = 'Security Vulnerabilities';
  public static description = 'Sum of known, unresolved security vulnerabilities for all associated components, including ' +
    'vulnerabilities with high, critical and medium severity. Security vulnerabilities are collected by Hygieia from various ' +
    'software security scanning tools.';
  public static dataSource = 'Data Source: OneSecurity Portal';
}
