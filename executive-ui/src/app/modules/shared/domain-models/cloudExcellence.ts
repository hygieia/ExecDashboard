import { AwsArchitectureScoreRequest } from "./awsArchitectureScoreRequest";
import { AwsNonProdScoreRequest } from "./awsNonProdScoreRequest";
import { AwsProdScoreRequest } from "./awsProdScoreRequest";

export class CloudExcellence {
	nonProdMigrationPoints: number;
	stageMigrationPoints: number;
	prodMigrationPoints: number;
	architecturePoints: number;
	presentationPoints: number;
	totalPoints: number;
	totalImprovements: number;
	
	
    awsArchitectureScoreRequest : AwsArchitectureScoreRequest;
    awsNonProdScoreRequest :AwsNonProdScoreRequest;
    awsProdScoreRequest : AwsProdScoreRequest;
}