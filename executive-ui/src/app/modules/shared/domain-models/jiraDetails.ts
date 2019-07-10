import { CommitDetails } from './commitDetails';

export class JiraDetails {
  linesDeleted: number;
  linesAdded: number;
  jiraID: string;  
  commitDetails: CommitDetails[];
}