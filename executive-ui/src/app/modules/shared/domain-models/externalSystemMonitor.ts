import { ExternalMetric } from './externalMetric';

export class ExternalSystemMonitor {
  sourceSystemName: string;
  impaactedExecutiveCard: string[];
  sourceSystemType: string;
  overallStatus: boolean;  
  lastConnectedTime: number; 
  metrics: ExternalMetric[];
  connectionCredentials: string[];	
  	
}