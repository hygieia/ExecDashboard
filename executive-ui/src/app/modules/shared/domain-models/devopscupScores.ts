import { EngineeringExcellence } from "./engineeringExcellence";
import { CloudExcellence } from "./cloudExcellence";

export class DevopscupScores {
   	appId :String;
   	appName :String;
	portfolio :String;
	active:boolean;
	timeStamp : number;
	quarter :number;
	enggExcelPoints :number;
	enggExcelPercent :number;
	enggExcelTrend :number ;
	cloudExcelPoints :number;
	cloudExcelPercent :number;
   	cloudExcelTrend :number;
	totalPoints :number ;
	totalPercent :number;
	enggExcel : EngineeringExcellence;
	cloudExcel :CloudExcellence; 
	enggExcelActive : boolean;
	cloudExcelActive :boolean;
	rank : number;
}