import { JiraDetails } from './jiraDetails';

export class ApplicationDetails {
  linesDeleted: number;
  linesAdded: number;
  applicationName: string;
  applicationId: string;
  irCount : number;
  jiraDetails: JiraDetails[];
}
