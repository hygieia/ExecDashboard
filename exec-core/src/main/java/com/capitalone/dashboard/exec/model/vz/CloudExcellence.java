package com.capitalone.dashboard.exec.model.vz;

/**
 * CloudExcellence added for DevopsCup Scores
 * 
 * @author RHE94MG
 *
 */
public class CloudExcellence {

	private Integer nonProdMigrationPoints;
	private Integer stageMigrationPoints;
	private Integer prodMigrationPoints;
	private Integer architecturePoints;
	private Integer presentationPoints;
	private Integer totalPoints;
	private Double totalImprovements;
	private AwsArchitectureScoreRequest awsArchitectureScoreRequest;
	private AwsNonProdScoreRequest awsNonProdScoreRequest;
	private AwsProdScoreRequest awsProdScoreRequest;

	public Integer getNonProdMigrationPoints() {
		return nonProdMigrationPoints;
	}

	public void setNonProdMigrationPoints(Integer nonProdMigrationPoints) {
		this.nonProdMigrationPoints = nonProdMigrationPoints;
	}

	public Integer getStageMigrationPoints() {
		return stageMigrationPoints;
	}

	public void setStageMigrationPoints(Integer stageMigrationPoints) {
		this.stageMigrationPoints = stageMigrationPoints;
	}

	public Integer getProdMigrationPoints() {
		return prodMigrationPoints;
	}

	public void setProdMigrationPoints(Integer prodMigrationPoints) {
		this.prodMigrationPoints = prodMigrationPoints;
	}

	public Integer getArchitecturePoints() {
		return architecturePoints;
	}

	public void setArchitecturePoints(Integer architecturePoints) {
		this.architecturePoints = architecturePoints;
	}

	public Integer getPresentationPoints() {
		return presentationPoints;
	}

	public void setPresentationPoints(Integer presentationPoints) {
		this.presentationPoints = presentationPoints;
	}

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Double getTotalImprovements() {
		return totalImprovements;
	}

	public void setTotalImprovements(Double totalImprovements) {
		this.totalImprovements = totalImprovements;
	}

	public AwsArchitectureScoreRequest getAwsArchitectureScoreRequest() {
		return awsArchitectureScoreRequest;
	}

	public void setAwsArchitectureScoreRequest(AwsArchitectureScoreRequest awsArchitectureScoreRequest) {
		this.awsArchitectureScoreRequest = awsArchitectureScoreRequest;
	}

	public AwsNonProdScoreRequest getAwsNonProdScoreRequest() {
		return awsNonProdScoreRequest;
	}

	public void setAwsNonProdScoreRequest(AwsNonProdScoreRequest awsNonProdScoreRequest) {
		this.awsNonProdScoreRequest = awsNonProdScoreRequest;
	}

	public AwsProdScoreRequest getAwsProdScoreRequest() {
		return awsProdScoreRequest;
	}

	public void setAwsProdScoreRequest(AwsProdScoreRequest awsProdScoreRequest) {
		this.awsProdScoreRequest = awsProdScoreRequest;
	}

}
